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

import java.util.HashMap;
import java.util.List;
import java.util.Set;
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
        if (requestDto.foodCategoryIds().size() != foodCategoryRepository.findAllByIds(requestDto.foodCategoryIds()).size()) {
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
        return getFoodById(savedFoodEntity.getId());
    }

    public GetFoodOut getFoodById(Long id) {
        return foodRepository.findDtoById(id)
                .map(foodDto -> new GetFoodOut(
                        foodDto.id(),
                        foodDto.name(),
                        foodDto.description(),
                        foodDto.image(),
                        foodDto.dietaryRestrictions(),
                        foodDto.price(),
                        foodDto.pickupTime(),
                        foodDto.productType(),
                        foodDto.productProviderName(),
                        foodCategoryRepository.findCategoriesForFoodById(foodDto.id())
                )).orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id));
    }

    public List<GetFoodOut> getAllByFoodCategoryIds(Set<Long> foodCategoryIds) {
        return foodRepository.findAllByFoodCategoryIds(foodCategoryIds).stream()
                .map(foodDto -> new GetFoodOut(
                        foodDto.id(),
                        foodDto.name(),
                        foodDto.description(),
                        foodDto.image(),
                        foodDto.dietaryRestrictions(),
                        foodDto.price(),
                        foodDto.pickupTime(),
                        foodDto.productType(),
                        foodDto.productProviderName(),
                        foodCategoryRepository.findCategoriesForFoodById(foodDto.id())
                )).collect(Collectors.toList());
    }

    public List<GetFoodOut> getAll() {
        final HashMap<Long, Set<FoodCategoryDto>> map = new HashMap<>();
        for (FoodCategoryDto dto : foodCategoryRepository.findCategoriesForAllFoods()) {
            if (map.containsKey(dto.foodId())) {
                map.get(dto.foodId()).add(dto);
            } else {
                map.put(dto.foodId(), Set.of(dto));
            }
        }

        return foodRepository.findAllDto().stream()
                .map(foodDto -> new GetFoodOut(
                        foodDto.id(),
                        foodDto.name(),
                        foodDto.description(),
                        foodDto.image(),
                        foodDto.dietaryRestrictions(),
                        foodDto.price(),
                        foodDto.pickupTime(),
                        foodDto.productType(),
                        foodDto.productProviderName(),
                        map.get(foodDto.id())
                )).collect(Collectors.toList());
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
