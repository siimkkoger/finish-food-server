package com.ffreaky.shoppingservice.food.repository;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.GetFoodDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, Long> {

    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.GetFoodDto(
                f.id,
                f.name,
                f.description,
                f.image,
                f.dietaryRestrictions,
                f.product.price,
                f.product.pickupTime,
                f.product.productCategory.productCategoryName,
                f.product.productProvider.name)
            FROM FoodEntity f WHERE f.id = :id
            """)
    Optional<GetFoodDto> findByIdCustom(long id);


}
