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

    // TODO - read more about optimistic locking
    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type_name", nullable = false, updatable = false)
    private ProductType productType;

    @Column(name = "dietary_restrictions", nullable = false, length = 1000)
    private String dietaryRestrictions;

}
