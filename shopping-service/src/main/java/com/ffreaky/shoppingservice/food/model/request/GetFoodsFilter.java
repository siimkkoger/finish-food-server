package com.ffreaky.shoppingservice.food.model.request;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

public record GetFoodsFilter(
        Set<Long> foodCategoryIds,
        String productProviderName,
        String dietaryRestrictions, // TODO - change to enum
        LocalDateTime createdAtFrom,
        LocalDateTime createdAtTo,
        Date pickupTimeFrom,
        Date pickupTimeTo) {

}
