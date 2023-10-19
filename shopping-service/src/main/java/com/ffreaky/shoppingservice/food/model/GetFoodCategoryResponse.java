package com.ffreaky.shoppingservice.food.model;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GetFoodCategoryResponse(@NotNull Set<FoodCategoryDto> foodCategories) {
}
