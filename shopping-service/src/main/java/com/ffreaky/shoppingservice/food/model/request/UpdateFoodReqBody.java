package com.ffreaky.shoppingservice.food.model.request;

import com.ffreaky.shoppingservice.product.model.request.UpdateProductReqBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


public record UpdateFoodReqBody(
        String dietaryRestrictions,
        @Valid @NotNull UpdateProductReqBody product
) {
}
