package com.example.finishfoodserver.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record FoodDTO(Long id, String name, BigDecimal price) {
}
