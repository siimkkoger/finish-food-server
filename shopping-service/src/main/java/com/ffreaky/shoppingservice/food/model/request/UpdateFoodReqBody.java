package com.ffreaky.shoppingservice.food.model.request;

import com.ffreaky.shoppingservice.product.model.request.UpdateProductReqBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


public record UpdateFoodReqBody(
        @NotNull Boolean vegetarian,
        @NotNull Boolean vegan,
        @NotNull Boolean glutenFree,
        @NotNull Boolean nutFree,
        @NotNull Boolean dairyFree,
        @NotNull Boolean organic,
        @Valid @NotNull UpdateProductReqBody product
) {
}
