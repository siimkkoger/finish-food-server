package com.ffreaky.shoppingservice.food.model;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateFoodCategoryRequest(
        @NotNull Long foodId,
        @NotNull Set<Long> foodCategoryIds
) {
}
