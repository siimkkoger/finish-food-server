package com.ffreaky.shoppingservice.food.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FoodFoodCategoryDto(@NotNull Long foodId, @NotNull Long foodCategoryId, @NotBlank String foodCategoryName) {
    public FoodFoodCategoryDto {
        assert foodId != null;
        assert foodCategoryId != null;
        assert foodCategoryName != null;
    }
}
