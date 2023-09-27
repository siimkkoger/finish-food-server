package com.ffreaky.shoppingservice.food.model;

import com.ffreaky.shoppingservice.product.entity.ProductTypeEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

public record FoodDto(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String image,
        @NotBlank String dietaryRestrictions,
        @NotNull BigDecimal price,
        @NotBlank Date pickupTime,
        @NotBlank ProductTypeEntity.ProductType productType,
        @NotBlank String productProviderName

        ) {
    public FoodDto {
        assert id != null;
        assert name != null;
        assert description != null;
        assert image != null;
        assert dietaryRestrictions != null;
        assert price != null;
        assert pickupTime != null;
        assert productType != null;
        assert productProviderName != null;
    }
}
