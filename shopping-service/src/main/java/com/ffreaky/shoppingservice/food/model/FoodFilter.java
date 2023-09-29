package com.ffreaky.shoppingservice.food.model;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record FoodFilter(Set<Long> foodCategoryIds, @NotNull Boolean includeFoodCategories) {
    public FoodFilter {
        assert foodCategoryIds != null;
        assert includeFoodCategories != null;
    }
}
