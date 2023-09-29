package com.ffreaky.shoppingservice.food.model;

import com.ffreaky.shoppingservice.product.model.CreateProductRequestDto;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateFoodRequest(
        @NotNull CreateProductRequestDto product,
        Set<Long> foodCategoryIds,
        String dietaryRestrictions,
        @NotNull FoodFilter filter
) {
}
