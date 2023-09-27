package com.ffreaky.shoppingservice.food.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FoodCategoryDto(@NotNull Long foodId, @NotNull Long foodCategoryId, @NotBlank String categoryName) {
    public FoodCategoryDto {
        assert foodId != null;
        assert foodCategoryId != null;
        assert categoryName != null;
    }
}
