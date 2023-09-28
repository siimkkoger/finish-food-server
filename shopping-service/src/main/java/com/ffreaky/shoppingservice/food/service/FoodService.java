package com.ffreaky.shoppingservice.food.service;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.entity.FoodFoodCategoryEntity;
import com.ffreaky.shoppingservice.food.model.*;
import com.ffreaky.shoppingservice.food.repository.FoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodFoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodRepository;
import com.ffreaky.shoppingservice.product.ProductType;
import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import com.ffreaky.shoppingservice.product.entity.ProductId;
import com.ffreaky.shoppingservice.product.repository.ProductRepository;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final ProductRepository productRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final FoodFoodCategoryRepository foodFoodCategoryRepository;

    public FoodService(
            FoodRepository foodRepository,
            ProductRepository productRepository,
            FoodCategoryRepository foodCategoryRepository,
            FoodFoodCategoryRepository foodFoodCategoryRepository) {
        this.foodRepository = foodRepository;
        this.productRepository = productRepository;
        this.foodCategoryRepository = foodCategoryRepository;
        this.foodFoodCategoryRepository = foodFoodCategoryRepository;
    }


    @Transactional
    public GetFoodResponse createFood(CreateFoodRequest reqBody) {
        // Check that product category is FOOD
        if (!reqBody.product().productType().equals(ProductType.FOOD)) {
            throw new FinishFoodException(FinishFoodException.Type.INVALID_PRODUCT_TYPE, "Product type must be FOOD");
        }

        // Create product
        final ProductEntity productEntity = new ProductEntity();
        final ProductId productId = new ProductId();
        productId.setProductType(ProductType.FOOD);
        productEntity.setProductId(productId);
        productEntity.setProductProviderId(reqBody.product().productProviderId());
        productEntity.setName(reqBody.name());
        productEntity.setDescription(reqBody.description());
        productEntity.setImage(reqBody.image());
        productEntity.setPrice(reqBody.product().price());
        productEntity.setPickupTime(reqBody.product().pickupTime());

        // Save product
        final ProductEntity savedProductEntity;
        try {
            savedProductEntity = productRepository.save(productEntity);
        } catch (Exception e) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Error saving product: " + e.getMessage());
        }

        // Check that food categories exist
        if (reqBody.foodCategoryIds().size() != foodCategoryRepository.findAllCatsByIds(reqBody.foodCategoryIds()).size()) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food category not found");
        }

        // Create food
        final FoodEntity fe = new FoodEntity();
        fe.setProductId(savedProductEntity.getProductId().getId());
        fe.setProductTypeName(ProductType.FOOD);
        fe.setDietaryRestrictions(reqBody.dietaryRestrictions());

        // Save food
        final FoodEntity savedFoodEntity;
        try {
            savedFoodEntity = foodRepository.save(fe);
        } catch (Exception e) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Error saving food: " + e.getMessage());
        }

        // Create food categories
        foodFoodCategoryRepository.saveAll(
                reqBody.foodCategoryIds().stream()
                        .map(id -> {
                            final FoodFoodCategoryEntity ffc = new FoodFoodCategoryEntity();
                            ffc.getId().setFoodId(savedFoodEntity.getId());
                            ffc.getId().setFoodCategoryId(id);
                            return ffc;
                        }).collect(Collectors.toList()));

        // Return GetFoodOut
        return getFoodById(savedFoodEntity.getId(), new GetFoodRequestFilter(null, true));
    }

    public GetFoodResponse getFoodById(Long id, GetFoodRequestFilter filter) {
        FoodDto foodDto = foodRepository.findDtoById(id)
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id));

        Set<FoodCategoryDto> foodCategories = filter != null && filter.includeFoodCategories()
                ? foodCategoryRepository.findCatByFoodId(foodDto.id())
                : null;

        return convertFoodDtoToGetFoodResponse(foodDto, foodCategories);
    }

    public List<GetFoodResponse> getAll(GetFoodRequestFilter filter) {
        // Get food and product information
        Set<FoodDto> foods = filter.foodCategoryIds() == null || filter.foodCategoryIds().isEmpty()
                ? foodRepository.findAllDto()
                : foodRepository.findAllByFoodCategoryIds(filter.foodCategoryIds());

        // Include categories to response if requested
        if (filter.includeFoodCategories()) {
            return addCatsToFoods(foods);
        }

        return foods.stream()
                .map(foodDto -> convertFoodDtoToGetFoodResponse(foodDto, null))
                .collect(Collectors.toList());
    }

    private List<GetFoodResponse> addCatsToFoods(Set<FoodDto> foods) {
        Map<Long, Set<FoodCategoryDto>> foodCategoryMap = new HashMap<>();
        Set<Long> foodIds = foods.stream()
                .map(FoodDto::id)
                .collect(Collectors.toSet());

        foodCategoryRepository.findCatsByFoodIds(foodIds)
                .forEach(dto -> foodCategoryMap.computeIfAbsent(dto.foodId(), k -> new HashSet<>()).add(dto));

        return foods.stream()
                .map(foodDto -> convertFoodDtoToGetFoodResponse(foodDto, foodCategoryMap.getOrDefault(foodDto.id(), Collections.emptySet())))
                .collect(Collectors.toList());
    }

    private GetFoodResponse convertFoodDtoToGetFoodResponse(FoodDto foodDto, Set<FoodCategoryDto> foodCategories) {
        return new GetFoodResponse(
                foodDto.id(),
                foodDto.name(),
                foodDto.description(),
                foodDto.image(),
                foodDto.dietaryRestrictions(),
                foodDto.price(),
                foodDto.pickupTime(),
                foodDto.productType(),
                foodDto.productProviderName(),
                foodCategories);
    }

    @Transactional
    public FoodDto updateFood(Long id, UpdateFoodRequest updatedFood) {
        return null;
    }

    public void deleteFood(Long id) {
        if (foodRepository.existsById(id)) {
            foodRepository.deleteById(id);
        } else {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id);
        }
    }

}
