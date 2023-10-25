package com.ffreaky.shoppingservice.product.entity;

import com.ffreaky.shoppingservice.product.ProductType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class ProductId implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type_name", nullable = false, updatable = false)
    private ProductType productType;

    public ProductId(Long id, ProductType productType) {
        this.id = id;
        this.productType = productType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId productId = (ProductId) o;
        return Objects.equals(id, productId.id) && productType == productId.productType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productType);
    }
}
