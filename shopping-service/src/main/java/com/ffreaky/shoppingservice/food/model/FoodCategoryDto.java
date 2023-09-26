package com.ffreaky.shoppingservice.food.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FoodCategoryDto(@NotNull Long id, @NotBlank String name) {
}
