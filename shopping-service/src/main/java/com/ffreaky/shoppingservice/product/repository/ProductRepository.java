package com.ffreaky.shoppingservice.product.repository;

import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT p FROM ProductEntity p JOIN FoodEntity f ON p.id = f.productId WHERE f.id = :foodId")
    ProductEntity findByFoodId(Long foodId);
}
