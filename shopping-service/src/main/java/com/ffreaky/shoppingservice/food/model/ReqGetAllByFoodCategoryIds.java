package com.ffreaky.shoppingservice.food.model;

import java.util.Set;

public record ReqGetAllByFoodCategoryIds(Set<Long> foodCategoryIds) {
}
