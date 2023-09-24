package com.ffreaky.shoppingservice.food.repository;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.GetFoodResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodRepository extends JpaRepository<FoodEntity, Long> {

    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.GetFoodResponseDto(
                f.id,
                f.name,
                f.description,
                f.image,
                f.dietaryRestrictions,
                f.product.price,
                f.product.pickupTime,
                f.product.productCategory.productCategory,
                f.product.productProvider.name
                )
            FROM FoodEntity f WHERE f.id = :id
            """)
    GetFoodResponseDto findByIdCustom(long id);

    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.GetFoodResponseDto(
                f.id,
                f.name,
                f.description,
                f.image,
                f.dietaryRestrictions,
                f.product.price,
                f.product.pickupTime,
                f.product.productCategory.productCategory,
                f.product.productProvider.name
                )
            FROM FoodEntity f
            """)
    List<GetFoodResponseDto> findAllCustom();
}
