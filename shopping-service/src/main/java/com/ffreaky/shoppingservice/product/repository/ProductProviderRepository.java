package com.ffreaky.shoppingservice.product.repository;

import com.ffreaky.shoppingservice.product.entity.ProductProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductProviderRepository extends JpaRepository<ProductProviderEntity, Long> {

}
