package com.ffreaky.shoppingservice.food.model.response;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GetFoodListResult(
        @NotNull List<GetFoodResponse> foods,
        @NotNull Integer page,
        @NotNull Integer pageSize,
        @NotNull Long totalItems,
        @NotNull Integer totalPages
) {
}
