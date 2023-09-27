package com.ffreaky.shoppingservice.food.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class FoodFoodCategoryId implements Serializable {

    @Column(name = "food_id", nullable = false, updatable = false)
    private Long foodId;
    @Column(name = "food_category_id", nullable = false, updatable = false)
    private Long foodCategoryId;

    public FoodFoodCategoryId(Long foodId, Long foodCategoryId) {
        this.foodId = foodId;
        this.foodCategoryId = foodCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodFoodCategoryId that = (FoodFoodCategoryId) o;
        return Objects.equals(foodId, that.foodId) && Objects.equals(foodCategoryId, that.foodCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodId, foodCategoryId);
    }
}
