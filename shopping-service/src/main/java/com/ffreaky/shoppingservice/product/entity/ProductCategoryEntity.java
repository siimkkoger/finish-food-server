package com.ffreaky.shoppingservice.product.entity;

import com.ffreaky.utilities.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product_category", schema = "public")
public class ProductCategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private ProductCategoryName productCategoryName;

    public enum ProductCategoryName {
        FOOD,
        CLOTHES,
    }
}
