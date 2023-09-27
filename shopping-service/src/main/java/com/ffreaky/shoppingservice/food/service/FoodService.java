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
    public GetFoodOut createFood(CreateFoodRequestDto requestDto) {
        // Check that product category is FOOD
        if (!requestDto.product().productType().equals(ProductType.FOOD)) {
            throw new FinishFoodException(FinishFoodException.Type.INVALID_PRODUCT_TYPE, "Product type must be FOOD");
        }

        // Create product
        final ProductEntity productEntity = new ProductEntity();
        final ProductId productId = new ProductId();
        productId.setProductType(ProductType.FOOD);
        productEntity.setProductId(productId);
        productEntity.setProductProviderId(requestDto.product().productProviderId());
        productEntity.setName(requestDto.name());
        productEntity.setDescription(requestDto.description());
        productEntity.setImage(requestDto.image());
        productEntity.setPrice(requestDto.product().price());
        productEntity.setPickupTime(requestDto.product().pickupTime());

        // Save product
        final ProductEntity savedProductEntity;
        try {
            savedProductEntity = productRepository.save(productEntity);
        } catch (Exception e) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Error saving product: " + e.getMessage());
        }

        // Check that food categories exist and retrieve them
        if (requestDto.foodCategoryIds().size() != foodCategoryRepository.findAllCatsByIds(requestDto.foodCategoryIds()).size()) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food category not found");
        }

        // Create food
        final FoodEntity foodEntity = new FoodEntity();
        foodEntity.setProductId(savedProductEntity.getProductId().getId());
        foodEntity.setProductTypeName(ProductType.FOOD);
        foodEntity.setDietaryRestrictions(requestDto.dietaryRestrictions());

        // Save food
        final FoodEntity savedFoodEntity;
        try {
            savedFoodEntity = foodRepository.save(foodEntity);
        } catch (Exception e) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Error saving food: " + e.getMessage());
        }

        // Create food categories
        foodFoodCategoryRepository.saveAll(requestDto.foodCategoryIds().stream()
                .map(foodCategoryId -> {
                    final FoodFoodCategoryEntity foodFoodCategoryEntity = new FoodFoodCategoryEntity();
                    foodFoodCategoryEntity.getId().setFoodId(savedFoodEntity.getId());
                    foodFoodCategoryEntity.getId().setFoodCategoryId(foodCategoryId);
                    return foodFoodCategoryEntity;
                }).collect(Collectors.toList()));

        // Return GetFoodOut
        return getFoodById(savedFoodEntity.getId(), null);
    }

    public GetFoodOut getFoodById(Long id, GetFoodsFilter filter) {
        FoodDto foodDto = foodRepository.findDtoById(id)
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id));

        Set<FoodCategoryDto> foodCategories = filter != null && filter.includeFoodCategories()
                ? foodCategoryRepository.findCatByFoodId(foodDto.id())
                : null;

        return convertFoodDtoToGetFoodOut(foodDto, foodCategories);
    }

    public List<GetFoodOut> getAll(GetFoodsFilter filter) {
        Set<FoodDto> foods;

        // Get all foods if no categories requested
        if (filter.foodCategoryIds() == null || filter.foodCategoryIds().isEmpty()) {
            foods = foodRepository.findAllDto();
        } else {
            foods = foodRepository.findAllByFoodCategoryIds(filter.foodCategoryIds());
        }

        // Include categories to response if requested
        if (filter.includeFoodCategories()) {
            return addCategoriesAndReturnResponse(foods);
        } else {
            return foods.stream()
                    .map(foodDto -> convertFoodDtoToGetFoodOut(foodDto, null))
                    .collect(Collectors.toList());
        }
    }

    private List<GetFoodOut> addCategoriesAndReturnResponse(Set<FoodDto> foods) {
        Map<Long, Set<FoodCategoryDto>> foodCategoryMap = new HashMap<>();
        Set<Long> foodIds = foods.stream()
                .map(FoodDto::id)
                .collect(Collectors.toSet());

        foodCategoryRepository.findCatsByFoodIds(foodIds)
                .forEach(dto -> foodCategoryMap.computeIfAbsent(dto.foodId(), k -> new HashSet<>()).add(dto));

        return foods.stream()
                .map(foodDto -> convertFoodDtoToGetFoodOut(foodDto, foodCategoryMap.getOrDefault(foodDto.id(), Collections.emptySet())))
                .collect(Collectors.toList());
    }

    private GetFoodOut convertFoodDtoToGetFoodOut(FoodDto foodDto, Set<FoodCategoryDto> foodCategories) {
        return new GetFoodOut(
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
    public FoodDto updateFood(Long id, UpdateFoodRequestDto updatedFood) {
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
