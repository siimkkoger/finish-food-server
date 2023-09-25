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
@Table(name = "product_provider_type", schema = "public")
public class ProductProviderTypeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private ProductProviderType productProviderType;

    enum ProductProviderType {
        RESTAURANT,
        SHOP,
    }
}
