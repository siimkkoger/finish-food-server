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
@Table(name = "product_type", schema = "public")
public class ProductTypeEntity extends BaseEntity {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private ProductType productType;

    public enum ProductType {
        FOOD,
        CLOTHES
    }
}
