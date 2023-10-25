package com.ffreaky.shoppingservice.product.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateProductReqBody(
        @NotBlank String name,
        String description,
        String image,
        @NotNull @Positive BigDecimal price,
        @NotNull LocalDateTime pickupTime
) {
}
