package com.ffreaky.shoppingservice.food.model.request;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateFoodFoodCategoriesReqBody(@NotNull Long foodId, @NotNull Set<Long> foodCategoryIds) {
}
