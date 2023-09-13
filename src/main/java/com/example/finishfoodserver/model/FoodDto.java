package com.example.finishfoodserver.model;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record FoodDto(@NotBlank Long id, @NotBlank String name, @NotBlank BigDecimal price) {
}
