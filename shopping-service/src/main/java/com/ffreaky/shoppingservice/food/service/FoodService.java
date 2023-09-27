package com.ffreaky.shoppingservice.food.service;

import com.ffreaky.shoppingservice.food.model.*;
import com.ffreaky.shoppingservice.food.repository.FoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodRepository;
import com.ffreaky.shoppingservice.product.repository.ProductProviderRepository;
import com.ffreaky.shoppingservice.product.repository.ProductRepository;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final ProductRepository productRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final ProductProviderRepository productProviderRepository;

    public FoodService(
            FoodRepository foodRepository,
            ProductRepository productRepository,
            FoodCategoryRepository foodCategoryRepository,
            ProductProviderRepository productProviderRepository) {
        this.foodRepository = foodRepository;
        this.productRepository = productRepository;
        this.foodCategoryRepository = foodCategoryRepository;
        this.productProviderRepository = productProviderRepository;
    }


    @Transactional
    public FoodDto createFood(CreateFoodRequestDto requestDto) {
        /*
        // Check that product category exists and retrieve it
        final ProductTypeEntity productTypeEntity =
                productCategoryRepository.findByProductType(requestDto.product().productType())
                        .orElseThrow(() -> new FinishFoodException(
                                FinishFoodException.Type.ENTITY_NOT_FOUND,
                                "Product category not found with categoryName: " + requestDto.product().productType()));

        // Check that product provider exists and retrieve it
        final ProductProviderEntity productProviderEntity =
                productProviderRepository.findById(requestDto.product().productProviderId())
                        .orElseThrow(() -> new FinishFoodException(
                                FinishFoodException.Type.ENTITY_NOT_FOUND,
                                "Product provider not found with foodCategoryId: " + requestDto.product().productProviderId()));

        // Create product
        final ProductEntity productEntity = new ProductEntity();
        productEntity.setProductType(productTypeEntity);
        productEntity.setProductProvider(productProviderEntity);
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

        // Create food
        final FoodEntity foodEntity = new FoodEntity();
        foodEntity.setProduct(savedProductEntity);
        foodEntity.setFoodCategories(foodCategoryEntities);
        foodEntity.setName(requestDto.categoryName());
        foodEntity.setDescription(requestDto.description());
        foodEntity.setImage(requestDto.image());
        foodEntity.setDietaryRestrictions(requestDto.dietaryRestrictions());
        foodEntity.setFoodCategories(foodCategoryEntities);

        // Save food
        final FoodEntity savedFoodEntity;
        try {
            savedFoodEntity = foodRepository.save(foodEntity);
        } catch (Exception e) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Error saving food: " + e.getMessage());
        }
        return new FoodDto(
                savedFoodEntity.getId(),
                savedFoodEntity.getName(),
                savedFoodEntity.getDescription(),
                savedFoodEntity.getImage(),
                savedFoodEntity.getDietaryRestrictions(),
                savedFoodEntity.getProduct().getPrice(),
                savedFoodEntity.getProduct().getPickupTime(),
                savedFoodEntity.getProduct().getProductType().getProductType(),
                savedFoodEntity.getProduct().getProductProvider().getName());
         */
        return null;
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

    public List<GetFoodOut> getAllFoods() {
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
