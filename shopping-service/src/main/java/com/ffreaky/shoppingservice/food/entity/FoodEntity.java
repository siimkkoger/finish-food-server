package com.ffreaky.shoppingservice.food.entity;

import com.ffreaky.shoppingservice.product.ProductEntity;
import com.ffreaky.utilities.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "food", schema = "public")
public class FoodEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = ProductEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToMany(targetEntity = FoodCategoryEntity.class, fetch = FetchType.LAZY)
    @JoinTable(name = "food_food_category")
    private Set<FoodCategoryEntity> foodCategories;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "dietary_restrictions", nullable = false, length = 1000)
    private String dietaryRestrictions;

}
