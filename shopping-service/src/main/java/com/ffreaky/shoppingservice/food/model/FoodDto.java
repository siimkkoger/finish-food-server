package com.ffreaky.shoppingservice.food.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FoodDto(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String image,
        @NotBlank String dietaryRestrictions
) {
}
