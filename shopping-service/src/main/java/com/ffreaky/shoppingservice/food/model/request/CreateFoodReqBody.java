package com.ffreaky.shoppingservice.food.model.request;

import com.ffreaky.shoppingservice.product.model.request.CreateProductReqBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateFoodReqBody(
        @Valid @NotNull CreateProductReqBody product,
        @NotNull Boolean vegetarian,
        @NotNull Boolean vegan,
        @NotNull Boolean glutenFree,
        @NotNull Boolean nutFree,
        @NotNull Boolean dairyFree,
        @NotNull Boolean organic
) {
}
