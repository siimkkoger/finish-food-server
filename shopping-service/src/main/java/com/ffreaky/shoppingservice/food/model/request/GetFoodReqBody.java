package com.ffreaky.shoppingservice.food.model.request;

import jakarta.validation.constraints.NotNull;

public record GetFoodReqBody(@NotNull Long foodId, @NotNull Boolean includeFoodCategories) {
}
