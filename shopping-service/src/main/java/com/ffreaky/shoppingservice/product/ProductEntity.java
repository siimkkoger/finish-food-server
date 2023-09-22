package com.ffreaky.shoppingservice.product;

import com.ffreaky.utilities.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "product", schema = "public")
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategoryEntity productCategory;

    @ManyToOne
    @JoinColumn(name = "product_provider_id")
    private ProductProviderEntity productProvider;

    @Column(name = "pickup_time", nullable = false)
    private Date pickupTime;

}
