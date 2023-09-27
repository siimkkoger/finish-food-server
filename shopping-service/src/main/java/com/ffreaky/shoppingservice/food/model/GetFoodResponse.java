package com.ffreaky.shoppingservice.food.model;

import com.ffreaky.shoppingservice.product.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public record GetFoodResponse(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String image,
        @NotBlank String dietaryRestrictions,
        @NotNull BigDecimal price,
        @NotBlank Date pickupTime,
        @NotBlank ProductType productType,
        @NotBlank String productProviderName,
        Set<FoodCategoryDto> foodCategories
) {
}
