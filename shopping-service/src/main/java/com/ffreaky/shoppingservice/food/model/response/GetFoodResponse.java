package com.ffreaky.shoppingservice.food.model.response;

import com.ffreaky.shoppingservice.product.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetFoodResponse(
        @NotNull Long foodId,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String image,
        @NotBlank Boolean vegetarian,
        @NotBlank Boolean vegan,
        @NotBlank Boolean glutenFree,
        @NotBlank Boolean nutFree,
        @NotBlank Boolean dairyFree,
        @NotBlank Boolean organic,
        @NotNull BigDecimal price,
        @NotBlank LocalDateTime pickupTime,
        @NotBlank ProductType productType,
        @NotBlank String productProviderName
) {
}
