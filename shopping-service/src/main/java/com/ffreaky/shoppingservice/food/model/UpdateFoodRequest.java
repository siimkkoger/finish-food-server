package com.ffreaky.shoppingservice.food.model;

import com.ffreaky.shoppingservice.product.model.UpdateProductRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateFoodRequest(
        @NotBlank UpdateProductRequestDto product,
        @NotNull Set<Long> foodCategoryIds,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String dietaryRestrictions,
        @NotBlank String image
) {
}
