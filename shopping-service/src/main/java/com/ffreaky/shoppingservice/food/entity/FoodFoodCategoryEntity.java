package com.ffreaky.shoppingservice.food.entity;

import com.ffreaky.utilities.entities.BaseEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "food_food_category", schema = "public")
public class FoodFoodCategoryEntity extends BaseEntity {

    @EmbeddedId
    private FoodFoodCategoryId id;

}
