package com.ffreaky.shoppingservice.product.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Date;

public record UpdateProductRequestDto(
        @NotNull @Positive BigDecimal price,
        @NotNull Date pickupTime
) {
}
