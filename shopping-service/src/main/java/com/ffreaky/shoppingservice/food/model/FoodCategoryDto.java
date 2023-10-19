package com.ffreaky.shoppingservice.food.model;

import jakarta.validation.constraints.NotNull;

public record FoodCategoryDto(@NotNull Long foodCategoryId, @NotNull String categoryName) {
}
