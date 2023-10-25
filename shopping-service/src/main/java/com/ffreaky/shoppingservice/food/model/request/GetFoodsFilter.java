package com.ffreaky.shoppingservice.food.model.request;

import java.time.LocalDateTime;
import java.util.Set;

public record GetFoodsFilter(
        Set<Long> foodCategoryIds,
        String productProviderName,
        String dietaryRestrictions, // TODO - change to enum
        LocalDateTime createdAtFrom,
        LocalDateTime createdAtTo,
        LocalDateTime pickupTimeFrom,
        LocalDateTime pickupTimeTo,
        Integer page,   // Page number (1-based)
        Integer pageSize // Number of items per page
) {
}
