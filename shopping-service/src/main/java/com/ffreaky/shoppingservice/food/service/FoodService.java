package com.ffreaky.shoppingservice.food.service;

import com.ffreaky.shoppingservice.food.entity.FoodCategoryEntity;
import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.*;
import com.ffreaky.shoppingservice.food.repository.FoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodRepository;
import com.ffreaky.shoppingservice.product.entity.ProductTypeEntity;
import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import com.ffreaky.shoppingservice.product.entity.ProductProviderEntity;
import com.ffreaky.shoppingservice.product.repository.ProductTypeRepository;
import com.ffreaky.shoppingservice.product.repository.ProductProviderRepository;
import com.ffreaky.shoppingservice.product.repository.ProductRepository;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final ProductRepository productRepository;
    private final ProductTypeRepository productCategoryRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final ProductProviderRepository productProviderRepository;

    public FoodService(
            FoodRepository foodRepository,
            ProductRepository productRepository,
            ProductTypeRepository productCategoryRepository,
            FoodCategoryRepository foodCategoryRepository,
            ProductProviderRepository productProviderRepository) {
        this.foodRepository = foodRepository;
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.foodCategoryRepository = foodCategoryRepository;
        this.productProviderRepository = productProviderRepository;
    }


    @Transactional
    public FoodDto createFood(CreateFoodRequestDto requestDto) {
        // Check that product category exists and retrieve it
        final ProductTypeEntity productTypeEntity =
                productCategoryRepository.findByProductType(requestDto.product().productType())
                        .orElseThrow(() -> new FinishFoodException(
                                FinishFoodException.Type.ENTITY_NOT_FOUND,
                                "Product category not found with name: " + requestDto.product().productType()));

        // Check that product provider exists and retrieve it
        final ProductProviderEntity productProviderEntity =
                productProviderRepository.findById(requestDto.product().productProviderId())
                        .orElseThrow(() -> new FinishFoodException(
                                FinishFoodException.Type.ENTITY_NOT_FOUND,
                                "Product provider not found with id: " + requestDto.product().productProviderId()));

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
        final Set<FoodCategoryEntity> foodCategoryEntities = foodCategoryRepository.findAllByIds(requestDto.foodCategoryIds());
        if (foodCategoryEntities.size() != requestDto.foodCategoryIds().size()) {
            throw new FinishFoodException(
                    FinishFoodException.Type.ENTITY_NOT_FOUND,
                    "Invalid food category IDs: " + requestDto.foodCategoryIds());
        }

        // Create food
        final FoodEntity foodEntity = new FoodEntity();
        foodEntity.setProduct(savedProductEntity);
        foodEntity.setFoodCategories(foodCategoryEntities);
        foodEntity.setName(requestDto.name());
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
    }

    public GetFoodOut getFoodById(Long id) {
        // TODO : can i send two requests to the database at the same time
        FoodDto foodDto = foodRepository.findDtoById(id)
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with id: " + id));

        Set<FoodCategoryDto> foodCategories = foodCategoryRepository.findAllByFoodsId(id)
                .stream()
                .map(entity -> new FoodCategoryDto(entity.getId(), entity.getName()))
                .collect(Collectors.toSet());

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

    // TODO : why does hibernate make different request for every entity even with eager fetching?
    public List<GetFoodOut> getAllFoods() {
        List<FoodEntity> foodEntities = foodRepository.findAll();
        System.out.println("888888888888888888888888888");
        return foodEntities.stream()
                .map(e -> new GetFoodOut(
                        e.getId(),
                        e.getName(),
                        e.getDescription(),
                        e.getImage(),
                        e.getDietaryRestrictions(),
                        e.getProduct().getPrice(),
                        e.getProduct().getPickupTime(),
                        e.getProduct().getProductType().getProductType(),
                        e.getProduct().getProductProvider().getName(),
                        e.getFoodCategories().stream().map(fce -> new FoodCategoryDto(fce.getId(), fce.getName())).collect(Collectors.toSet())
                )).collect(Collectors.toList());
    }

    @Transactional
    public FoodDto updateFood(Long id, UpdateFoodRequestDto updatedFood) {
        // Find the food to update
        FoodEntity foodEntity = foodRepository.findById(id)
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id));

        foodEntity.getProduct().setPrice(updatedFood.product().price());
        foodEntity.getProduct().setPickupTime(updatedFood.product().pickupTime());

        // Check that food categories exist and retrieve them
        final Set<FoodCategoryEntity> foodCategoryEntities = foodCategoryRepository.findAllByIds(updatedFood.foodCategoryIds());
        if (foodCategoryEntities.size() != updatedFood.foodCategoryIds().size()) {
            throw new FinishFoodException(
                    FinishFoodException.Type.ENTITY_NOT_FOUND,
                    "Invalid food category IDs: " + updatedFood.foodCategoryIds());
        }
        foodEntity.setFoodCategories(foodCategoryEntities);

        // Add rest of the updated fields
        foodEntity.setName(updatedFood.name());
        foodEntity.setDescription(updatedFood.description());
        foodEntity.setDietaryRestrictions(updatedFood.dietaryRestrictions());
        foodEntity.setImage(updatedFood.image());

        // Save the updated food
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
    }

    public void deleteFood(Long id) {
        if (foodRepository.existsById(id)) {
            foodRepository.deleteById(id);
        } else {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id);
        }
    }

}
