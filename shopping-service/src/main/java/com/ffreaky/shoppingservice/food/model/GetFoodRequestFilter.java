package com.ffreaky.shoppingservice.food.model;

import java.util.Set;

public record GetFoodRequestFilter(Set<Long> foodCategoryIds, Boolean includeFoodCategories) {
    public GetFoodRequestFilter {
        assert foodCategoryIds != null;
        assert includeFoodCategories != null;
    }
}
