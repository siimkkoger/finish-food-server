package com.example.finishfoodserver.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FoodDto(
        @NotNull Long id,
        @NotBlank(message = "name must not be blank") String name,
        @NotNull BigDecimal price) {
}
