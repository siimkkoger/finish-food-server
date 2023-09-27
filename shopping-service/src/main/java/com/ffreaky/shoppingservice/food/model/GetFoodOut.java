package com.ffreaky.shoppingservice.food.model;

import com.ffreaky.shoppingservice.product.entity.ProductTypeEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public record GetFoodOut(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String image,
        @NotBlank String dietaryRestrictions,
        @NotNull BigDecimal price,
        @NotBlank Date pickupTime,
        @NotBlank ProductTypeEntity.ProductType productType,
        @NotBlank String productProviderName,
        @NotNull Set<FoodCategoryDto> foodCategories

) {
}
