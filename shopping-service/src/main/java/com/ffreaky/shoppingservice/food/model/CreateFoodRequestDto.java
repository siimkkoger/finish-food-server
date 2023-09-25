package com.ffreaky.shoppingservice.food.model;

import com.ffreaky.shoppingservice.product.model.CreateProductRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateFoodRequestDto(
        @NotBlank CreateProductRequestDto product,
        @NotNull Set<Long> foodCategoryIds,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String dietaryRestrictions,
        @NotBlank String image
) {
}
