package com.ffreaky.shoppingservice.food.entity;

import com.ffreaky.shoppingservice.product.ProductEntity;
import com.ffreaky.utilities.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToOne(targetEntity = ProductEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="id", unique=true, nullable=false, updatable=false)
    private ProductEntity product;

    @ManyToMany(targetEntity = FoodCategoryEntity.class, fetch = FetchType.LAZY)
    @JoinTable(name = "food_foods_category",
            joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "food_category_id"))
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
