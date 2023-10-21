package com.ffreaky.shoppingservice.food.model.request;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GetFoodsFilter(@NotNull Set<Long> foodCategoryIds) {

}
