package com.ffreaky.shoppingservice.product.entity;

import com.ffreaky.utilities.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product", schema = "public")
public class ProductEntity extends BaseEntity {

    @EmbeddedId
    private ProductId productId;

    @Column(name = "product_provider_id", nullable = false, updatable = false)
    private Long productProviderId;

    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(name = "pickup_time", nullable = false)
    private Date pickupTime;

}
