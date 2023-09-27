package com.ffreaky.shoppingservice.food.model;

import java.util.Set;

public record GetFoodsFilter(Set<Long> foodCategoryIds, Boolean includeFoodCategories) {
    public GetFoodsFilter {
        assert foodCategoryIds != null;
        assert includeFoodCategories != null;
    }
}
