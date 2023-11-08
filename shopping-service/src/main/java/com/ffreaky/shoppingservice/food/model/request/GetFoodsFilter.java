package com.ffreaky.shoppingservice.food.model.request;

import com.ffreaky.shoppingservice.product.ProductOrderBy;
import com.querydsl.core.types.Order;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Filter for getting foods.
 *
 * @param foodCategoryIds
 * @param foodCategoryIdsMatchAll // If true, only foods that have all of the specified food category IDs will be returned.
 * @param productProviderIds // If specified, only foods whose product provider ID is in this set will be returned.
 * @param pickupTimeFrom
 * @param pickupTimeTo
 * @param page
 * @param pageSize
 * @param orderBy
 * @param direction
 */
public record GetFoodsFilter(
        Set<Long> foodCategoryIds,
        Boolean foodCategoryIdsMatchAll,
        Set<Long> productProviderIds,
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
        if (foodCategoryIds == null) {
            foodCategoryIds = Set.of();
        }
        if (productProviderIds == null) {
            productProviderIds = Set.of();
        }
        if (foodCategoryIdsMatchAll == null) {
            foodCategoryIdsMatchAll = false;
        }
    }
}
