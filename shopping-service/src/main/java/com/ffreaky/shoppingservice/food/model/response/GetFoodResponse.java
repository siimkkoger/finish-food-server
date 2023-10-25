package com.ffreaky.shoppingservice.food.model.response;

import com.ffreaky.shoppingservice.product.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetFoodResponse(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String image,
        @NotBlank String dietaryRestrictions,
        @NotNull BigDecimal price,
        @NotBlank LocalDateTime pickupTime,
        @NotBlank ProductType productType,
        @NotBlank String productProviderName
) {
}
