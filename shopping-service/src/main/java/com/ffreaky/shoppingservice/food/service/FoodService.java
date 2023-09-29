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
import com.ffreaky.shoppingservice.product.service.ProductService;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final FoodFoodCategoryRepository foodFoodCategoryRepository;
    private final ProductService productService;

    public FoodService(
            FoodRepository foodRepository,
            FoodCategoryRepository foodCategoryRepository,
            FoodFoodCategoryRepository foodFoodCategoryRepository,
            ProductService productService) {
        this.foodRepository = foodRepository;
        this.foodCategoryRepository = foodCategoryRepository;
        this.foodFoodCategoryRepository = foodFoodCategoryRepository;
        this.productService = productService;
    }


    /**
     * Add a new food row to the database.
     * The method also adds everything else that is needed for the food to be complete:
     * 1. ProductEntity
     * 2. FoodEntity
     * 3. FoodFoodCategoryEntity
     *
     * @param reqBody
     * @return GetFoodResponse
     */
    @Transactional
    public GetFoodResponse createFood(CreateFoodRequest reqBody) {
        // Check that product category is FOOD
        if (!reqBody.product().productType().equals(ProductType.FOOD)) {
            throw new FinishFoodException(FinishFoodException.Type.INVALID_PRODUCT_TYPE, "Product type must be FOOD");
        }

        // Create and save product
        final ProductEntity savedProductEntity = productService.createProduct(reqBody.product());

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

        // Create and save food categories
        foodFoodCategoryRepository.saveAll(
                reqBody.foodCategoryIds().stream()
                        .map(id -> {
                            final FoodFoodCategoryEntity ffc = new FoodFoodCategoryEntity();
                            ffc.getId().setFoodId(savedFoodEntity.getId());
                            ffc.getId().setFoodCategoryId(id);
                            return ffc;
                        }).collect(Collectors.toList()));

        // Return GetFoodOut
        return getFoodById(savedFoodEntity.getId(), reqBody.filter().includeFoodCategories());
    }

    public GetFoodResponse getFoodById(Long id, boolean includeFoodCategories) {
        FoodDto foodDto = foodRepository.findDtoById(id)
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id));

        Set<FoodCategoryDto> foodCategories = includeFoodCategories ? foodCategoryRepository.findCatByFoodId(foodDto.id()) : null;

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
