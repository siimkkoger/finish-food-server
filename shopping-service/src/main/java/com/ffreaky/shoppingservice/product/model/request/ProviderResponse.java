package com.ffreaky.shoppingservice.product.model.request;

import jakarta.validation.constraints.NotNull;

public record ProviderResponse(@NotNull Long id, @NotNull String name) {
}
