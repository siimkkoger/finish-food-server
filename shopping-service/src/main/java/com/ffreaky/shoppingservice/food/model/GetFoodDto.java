package com.ffreaky.shoppingservice.food.model;

import com.ffreaky.shoppingservice.food.entity.FoodCategoryEntity;
import com.ffreaky.shoppingservice.product.ProductType;
import com.ffreaky.shoppingservice.product.entity.ProductCategoryEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public record GetFoodDto(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String image,
        @NotBlank String dietaryRestrictions,
        @NotNull BigDecimal price,
        @NotBlank Date pickupTime,
        @NotBlank ProductType productType,
        @NotBlank String productProviderName,
        @NotNull Set<FoodCategoryDto> foodCategories

        ) {
    public GetFoodDto {
        assert id != null;
        assert name != null;
        assert description != null;
        assert image != null;
        assert dietaryRestrictions != null;
        assert price != null;
        assert pickupTime != null;
        assert productType != null;
        assert productProviderName != null;
        assert foodCategories != null;
    }
}
