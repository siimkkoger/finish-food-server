package com.ffreaky.shoppingservice.food.model.request;

import com.ffreaky.shoppingservice.product.model.UpdateProductRequestDto;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateFoodReqBody(
        @NotNull Long foodId,
        @NotNull UpdateProductRequestDto product,
        Set<Long> foodCategoryIds,
        String dietaryRestrictions,
        @NotNull GetFoodsFilter filter
) {
}
