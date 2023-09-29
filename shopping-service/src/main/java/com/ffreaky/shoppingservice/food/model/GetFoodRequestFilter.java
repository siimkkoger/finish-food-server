package com.ffreaky.shoppingservice.food.model;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GetFoodRequestFilter(Set<Long> foodCategoryIds, @NotNull Boolean includeFoodCategories) {
    public GetFoodRequestFilter {
        assert foodCategoryIds != null;
        assert includeFoodCategories != null;
    }
}
