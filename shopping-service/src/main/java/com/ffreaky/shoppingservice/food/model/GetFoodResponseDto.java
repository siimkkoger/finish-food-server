package com.ffreaky.shoppingservice.food.model;

import com.ffreaky.shoppingservice.product.ProductCategoryEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

public record GetFoodResponseDto(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String image,
        @NotBlank String dietaryRestrictions,
        @NotNull BigDecimal price,
        @NotBlank Date pickupTime,
        @NotBlank ProductCategoryEntity.ProductCategory productCategory,
        @NotBlank String productProviderName

) {
    public GetFoodResponseDto {
        assert id != null;
        assert name != null;
        assert description != null;
        assert image != null;
        assert dietaryRestrictions != null;
        assert price != null;
        assert pickupTime != null;
        assert productCategory != null;
        assert productProviderName != null;
    }
}
