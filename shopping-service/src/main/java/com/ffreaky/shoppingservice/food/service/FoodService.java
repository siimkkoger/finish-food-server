package com.ffreaky.shoppingservice.food.service;

import com.ffreaky.shoppingservice.food.entity.FoodCategoryEntity;
import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.CreateFoodRequestDto;
import com.ffreaky.shoppingservice.food.model.FoodCategoryDto;
import com.ffreaky.shoppingservice.food.model.GetFoodDto;
import com.ffreaky.shoppingservice.food.model.UpdateFoodRequestDto;
import com.ffreaky.shoppingservice.food.repository.FoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodRepository;
import com.ffreaky.shoppingservice.product.entity.ProductCategoryEntity;
import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import com.ffreaky.shoppingservice.product.entity.ProductProviderEntity;
import com.ffreaky.shoppingservice.product.repository.ProductCategoryRepository;
import com.ffreaky.shoppingservice.product.repository.ProductProviderRepository;
import com.ffreaky.shoppingservice.product.repository.ProductRepository;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final ProductProviderRepository productProviderRepository;

    public FoodService(
            FoodRepository foodRepository,
            ProductRepository productRepository,
            ProductCategoryRepository productCategoryRepository,
            FoodCategoryRepository foodCategoryRepository,
            ProductProviderRepository productProviderRepository) {
        this.foodRepository = foodRepository;
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.foodCategoryRepository = foodCategoryRepository;
        this.productProviderRepository = productProviderRepository;
    }

    @Transactional
    public GetFoodDto createFood(CreateFoodRequestDto requestDto) {
        // Check that product category exists and retrieve it
        final ProductCategoryEntity productCategoryEntity =
                productCategoryRepository.findByProductCategory(requestDto.product().productCategory())
                        .orElseThrow(() -> new FinishFoodException(
                                FinishFoodException.Type.ENTITY_NOT_FOUND,
                                "Product category not found with name: " + requestDto.product().productCategory()));

        // Check that product provider exists and retrieve it
        final ProductProviderEntity productProviderEntity =
                productProviderRepository.findById(requestDto.product().productProviderId())
                        .orElseThrow(() -> new FinishFoodException(
                                FinishFoodException.Type.ENTITY_NOT_FOUND,
                                "Product provider not found with id: " + requestDto.product().productProviderId()));

        // Create product
        final ProductEntity productEntity = new ProductEntity();
        productEntity.setProductCategory(productCategoryEntity);
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

        // Save food
        final FoodEntity savedFoodEntity;
        try {
            savedFoodEntity = foodRepository.save(foodEntity);
        } catch (Exception e) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Error saving food: " + e.getMessage());
        }
        return new GetFoodDto(
                savedFoodEntity.getId(),
                savedFoodEntity.getName(),
                savedFoodEntity.getDescription(),
                savedFoodEntity.getImage(),
                savedFoodEntity.getDietaryRestrictions(),
                savedFoodEntity.getProduct().getPrice(),
                savedFoodEntity.getProduct().getPickupTime(),
                savedFoodEntity.getProduct().getProductCategory().getProductType(),
                savedFoodEntity.getProduct().getProductProvider().getName(),
                savedFoodEntity.getFoodCategories().stream().map(entity -> new FoodCategoryDto(entity.getId(), entity.getName())).collect(Collectors.toSet()));
    }

    public GetFoodDto getFoodById(Long id) {
        System.out.println("1111111111111111111111111111111111111111");
        System.out.println("1111111111111111111111111111111111111111");
        System.out.println("1111111111111111111111111111111111111111");

        final FoodEntity fe = foodRepository.findById(id)
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id));

        GetFoodDto dto = new GetFoodDto(
                fe.getId(),
                fe.getName(),
                fe.getDescription(),
                fe.getImage(),
                fe.getDietaryRestrictions(),
                fe.getProduct().getPrice(),
                fe.getProduct().getPickupTime(),
                fe.getProduct().getProductCategory().getProductType(),
                fe.getProduct().getProductProvider().getName(),
                fe.getFoodCategories().stream().map(entity -> new FoodCategoryDto(entity.getId(), entity.getName())).collect(Collectors.toSet()));
        System.out.println(dto);

        /*
        System.out.println("22222222222222222222222222222222222222222");
        System.out.println("22222222222222222222222222222222222222222");
        System.out.println("22222222222222222222222222222222222222222");

        final Optional<GetFoodResponseDto> foodEntity = foodRepository.findByIdCustom(id);
        if (foodEntity.isEmpty()) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id);
        }
        System.out.println(foodEntity.get());
        return foodEntity.get();
         */

        return dto;
    }

    public List<GetFoodDto> getAllFoods() {
        //return foodRepository.findAllCustom();

        return null;
    }

    @Transactional
    public GetFoodDto updateFood(Long id, UpdateFoodRequestDto updatedFood) {
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
        return new GetFoodDto(
                savedFoodEntity.getId(),
                savedFoodEntity.getName(),
                savedFoodEntity.getDescription(),
                savedFoodEntity.getImage(),
                savedFoodEntity.getDietaryRestrictions(),
                savedFoodEntity.getProduct().getPrice(),
                savedFoodEntity.getProduct().getPickupTime(),
                savedFoodEntity.getProduct().getProductCategory().getProductType(),
                savedFoodEntity.getProduct().getProductProvider().getName(),
                savedFoodEntity.getFoodCategories().stream().map(entity -> new FoodCategoryDto(entity.getId(), entity.getName())).collect(Collectors.toSet()));
    }

    public void deleteFood(Long id) {
        if (foodRepository.existsById(id)) {
            foodRepository.deleteById(id);
        } else {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id);
        }
    }

}
