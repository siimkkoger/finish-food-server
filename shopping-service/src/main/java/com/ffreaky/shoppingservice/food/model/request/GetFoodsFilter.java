package com.ffreaky.shoppingservice.food.model.request;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GetFoodsFilter(@NotNull Set<Long> foodCategoryIds, @NotNull Boolean includeFoodCategories) {
    public GetFoodsFilter {
        assert foodCategoryIds != null;
        assert includeFoodCategories != null;
    }
}
