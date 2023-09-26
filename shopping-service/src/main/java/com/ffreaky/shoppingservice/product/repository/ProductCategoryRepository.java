package com.ffreaky.shoppingservice.product.repository;

import com.ffreaky.shoppingservice.product.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {

    @Query("SELECT p FROM ProductCategoryEntity p WHERE p.productCategoryName = :productCategory")
    Optional<ProductCategoryEntity> findByProductCategory(ProductCategoryEntity.ProductCategoryName productCategory);
}
