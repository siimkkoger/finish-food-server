package com.ffreaky.shoppingservice.product.model;

import com.ffreaky.shoppingservice.product.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Date;

public record CreateProductRequestDto(
        @NotNull ProductType productType,
        @NotNull Long productProviderId,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String image,
        @NotNull @Positive BigDecimal price,
        @NotNull Date pickupTime
) {
}
