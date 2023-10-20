package com.ffreaky.shoppingservice.food.model.request;

import com.ffreaky.shoppingservice.product.model.CreateProductRequestDto;
import jakarta.validation.constraints.NotNull;

public record CreateFoodReqBody(
        @NotNull CreateProductRequestDto product,
        String dietaryRestrictions,
        @NotNull GetFoodsFilter filter
) {
}
