package com.ffreaky.shoppingservice.food.model.request;

import com.ffreaky.shoppingservice.product.model.request.CreateProductReqBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateFoodReqBody(
        @Valid @NotNull CreateProductReqBody product,
        String dietaryRestrictions
) {
}
