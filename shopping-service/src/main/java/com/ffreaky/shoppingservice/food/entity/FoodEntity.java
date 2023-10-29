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
    // @Version
    // private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type_name", nullable = false, updatable = false)
    private ProductType productType;

    @Column(name = "vegetarian", nullable = false)
    private Boolean vegetarian;

    @Column(name = "vegan", nullable = false)
    private Boolean vegan;

    @Column(name = "gluten_free", nullable = false)
    private Boolean glutenFree;

    @Column(name = "nut_free", nullable = false)
    private Boolean nutFree;

    @Column(name = "dairy_free", nullable = false)
    private Boolean dairyFree;

    @Column(name = "organic", nullable = false)
    private Boolean organic;

}
