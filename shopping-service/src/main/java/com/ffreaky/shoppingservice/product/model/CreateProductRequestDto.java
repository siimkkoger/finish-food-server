package com.ffreaky.shoppingservice.product.model;

import com.ffreaky.shoppingservice.product.ProductType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Date;

public record CreateProductRequestDto(
        @NotNull ProductType productType,
        @NotNull Long productProviderId,
        @NotNull @Positive BigDecimal price,
        @NotNull Date pickupTime
) {
}