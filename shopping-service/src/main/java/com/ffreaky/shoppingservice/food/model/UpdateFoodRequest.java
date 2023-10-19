package com.ffreaky.shoppingservice.food.model;

import com.ffreaky.shoppingservice.product.model.UpdateProductRequestDto;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateFoodRequest(
        @NotNull Long foodId,
        @NotNull UpdateProductRequestDto product,
        Set<Long> foodCategoryIds,
        String dietaryRestrictions,
        @NotNull FoodFilter filter
) {
}
