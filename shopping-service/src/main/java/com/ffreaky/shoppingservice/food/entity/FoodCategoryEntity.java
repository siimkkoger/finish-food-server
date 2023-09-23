package com.ffreaky.shoppingservice.food.entity;

import com.ffreaky.utilities.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "product", schema = "public")
public class FoodCategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(targetEntity = FoodEntity.class, mappedBy = "food_category", fetch = FetchType.LAZY)
    private Set<FoodEntity> foodCategories;
}
