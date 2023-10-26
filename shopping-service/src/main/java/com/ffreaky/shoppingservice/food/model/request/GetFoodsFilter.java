package com.ffreaky.shoppingservice.food.model.request;

import com.ffreaky.shoppingservice.product.ProductOrderBy;
import com.querydsl.core.types.Order;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.Set;

public record GetFoodsFilter(
        Set<Long> foodCategoryIds,
        String productProviderName, // TODO - change to id
        String dietaryRestrictions, // TODO - change to enum
        LocalDateTime pickupTimeFrom,
        LocalDateTime pickupTimeTo,
        @NotNull @Min(1) Integer page,   // Page number (1-based)
        @NotNull @Positive Integer pageSize,   // Number of items per page
        @NotNull ProductOrderBy orderBy,
        @NotNull Order direction
) {
    public GetFoodsFilter {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 100;
        }
        if (direction == null) {
            direction = Order.ASC;
        }
        if (orderBy == null) {
            orderBy = ProductOrderBy.CREATED_AT;
        }
    }
}
