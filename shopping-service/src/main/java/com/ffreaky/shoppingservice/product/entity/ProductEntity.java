package com.ffreaky.shoppingservice.product.entity;

import com.ffreaky.shoppingservice.product.ProductType;
import com.ffreaky.utilities.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product", schema = "public")
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type_name", nullable = false, updatable = false)
    private ProductType productType;

    @Column(name = "product_provider_id", nullable = false, updatable = false)
    private Long productProviderId;

    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "image", length = 300)
    private String image;

    @Column(name = "pickup_time", nullable = false)
    private LocalDateTime pickupTime;

}
