package com.ffreaky.shoppingservice.food.entity;

import com.ffreaky.shoppingservice.product.ProductType;
import com.ffreaky.utilities.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "food", schema = "public")
public class FoodEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, updatable = false)
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type_name", nullable = false, updatable = false)
    private ProductType productTypeName;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "dietary_restrictions", nullable = false, length = 1000)
    private String dietaryRestrictions;

}
