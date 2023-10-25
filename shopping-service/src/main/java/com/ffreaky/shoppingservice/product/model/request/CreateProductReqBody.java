package com.ffreaky.shoppingservice.product.model.request;

import com.ffreaky.shoppingservice.product.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateProductReqBody(
        @NotNull ProductType productType,
        @NotNull Long productProviderId,
        @NotBlank String name,
        String description,
        String image,
        @NotNull @Positive BigDecimal price,
        @NotNull LocalDateTime pickupTime
) {
}
