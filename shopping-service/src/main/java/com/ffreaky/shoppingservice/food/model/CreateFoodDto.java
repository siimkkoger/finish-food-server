package com.ffreaky.shoppingservice.food.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateFoodDto(
        @NotBlank String name,
        @NotNull BigDecimal price,
        @NotBlank String description,
        @NotBlank String image,
        @NotBlank String dietaryRestrictions
) {
}
