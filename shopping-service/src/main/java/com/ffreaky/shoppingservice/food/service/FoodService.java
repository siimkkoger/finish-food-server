package com.ffreaky.shoppingservice.food.service;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.entity.FoodFoodCategoryEntity;
import com.ffreaky.shoppingservice.food.model.*;
import com.ffreaky.shoppingservice.food.repository.FoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodFoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodRepository;
import com.ffreaky.shoppingservice.product.ProductType;
import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import com.ffreaky.shoppingservice.product.service.ProductService;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public GetFoodResponse createFood(CreateFoodRequest reqBody) {
        // Check that product category is FOOD
        if (!reqBody.product().productType().equals(ProductType.FOOD)) {
            throw new FinishFoodException(FinishFoodException.Type.INVALID_PRODUCT_TYPE, "Product type must be FOOD");
        }

        // Create and save product
        final ProductEntity savedProductEntity = productService.createProduct(reqBody.product());

        // Create food
        final FoodEntity fe = new FoodEntity();
        fe.setProductId(savedProductEntity.getProductId().getId());
        fe.setProductTypeName(ProductType.FOOD);
        fe.setDietaryRestrictions(reqBody.dietaryRestrictions());
        final FoodEntity savedFoodEntity = saveFoodEntity(fe);

        // Return GetFoodResponse
        return getFoodById(savedFoodEntity.getId(), reqBody.filter().includeFoodCategories());
    }

    private FoodEntity saveFoodEntity(FoodEntity fe) {
        final FoodEntity savedFoodEntity;
        try {
            savedFoodEntity = foodRepository.save(fe);
        } catch (Exception e) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Error saving food: " + e.getMessage());
        }
        return savedFoodEntity;
    }

    public GetFoodResponse getFoodById(Long id, boolean includeFoodCategories) {
        FoodDto foodDto = foodRepository.findDtoById(id)
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id));

        Set<FoodCategoryDto> foodCategorySet = includeFoodCategories ? foodCategoryRepository.findCategoriesByFoodId(foodDto.id()) : null;

        return convertFoodDtoToGetFoodResponse(foodDto, foodCategorySet);
    }

    public List<GetFoodResponse> getAll(FoodFilter filter) {
        // Get food and product information
        Set<FoodDto> foods = filter.foodCategoryIds() == null || filter.foodCategoryIds().isEmpty()
                ? foodRepository.findAllDto()
                : foodRepository.findAllByFoodCategoryIds(filter.foodCategoryIds());

        // Include categories to response if requested
        if (filter.includeFoodCategories()) {
            final var foodIds = foods.stream().map(FoodDto::id).collect(Collectors.toSet());
            final var foodIdCategoryMap = foodCategoryRepository.findCategoriesByFoodIds(foodIds)
                    .stream()
                    .collect(Collectors.groupingBy(
                            FoodFoodCategoryDto::foodId,
                            Collectors.mapping(
                                    ffcDto -> new FoodCategoryDto(ffcDto.foodCategoryId(), ffcDto.foodCategoryName()),
                                    Collectors.toSet()
                            )
                    ));

            return foods.stream()
                    .map(foodDto -> convertFoodDtoToGetFoodResponse(foodDto, foodIdCategoryMap.get(foodDto.id())))
                    .collect(Collectors.toList());
        }

        return foods.stream()
                .map(foodDto -> convertFoodDtoToGetFoodResponse(foodDto, Collections.emptySet()))
                .collect(Collectors.toList());
    }

    private GetFoodResponse convertFoodDtoToGetFoodResponse(FoodDto foodDto, Set<FoodCategoryDto> foodCategoryDtoSet) {
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
                foodCategoryDtoSet
        );
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public GetFoodResponse updateFood(UpdateFoodRequest req) {
        FoodEntity fe = foodRepository.findById(req.foodId())
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + req.foodId()));

        // Save product entity
        productService.updateProduct(fe.getProductId(), req.product());

        // Update food entity
        if (req.dietaryRestrictions().equals(fe.getDietaryRestrictions())) {
            fe.setDietaryRestrictions(req.dietaryRestrictions());
            saveFoodEntity(fe);
        }

        return getFoodById(req.foodId(), req.filter().includeFoodCategories());
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void createOrUpdateFoodCategories(UpdateFoodCategoryRequest req) {
        try {
            // Delete all food categories
            foodFoodCategoryRepository.deleteAll(foodFoodCategoryRepository.findAllByFoodId(req.foodId()));

            // Create and save food categories
            foodFoodCategoryRepository.saveAll(
                    req.foodCategoryIds().stream()
                            .map(id -> {
                                final FoodFoodCategoryEntity ffc = new FoodFoodCategoryEntity();
                                ffc.getId().setFoodId(req.foodId());
                                ffc.getId().setFoodCategoryId(id);
                                return ffc;
                            }).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new FinishFoodException(FinishFoodException.Type.BAD_REQUEST, "Error saving food categories: " + e.getMessage());
        }
    }

    public boolean deleteFood(Long id) {
        if (!foodRepository.existsById(id)) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id);
        }

        foodRepository.deleteById(id);
        return true;
    }

    public GetFoodCategoryResponse getAllFoodCategories() {
        return new GetFoodCategoryResponse(foodCategoryRepository.findAllCategories());
    }

    public GetFoodCategoryResponse getAllFoodCategoriesForFood(Long foodId) {
        return new GetFoodCategoryResponse(foodCategoryRepository.findCategoriesByFoodId(foodId));
    }

}
