package com.ffreaky.shoppingservice.food.model.response;

import com.ffreaky.shoppingservice.food.model.FoodCategoryDto;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GetFoodCategoryResponse(@NotNull Set<FoodCategoryDto> foodCategories) {
}
