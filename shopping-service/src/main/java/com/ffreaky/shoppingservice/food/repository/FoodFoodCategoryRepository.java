package com.ffreaky.shoppingservice.food.repository;

import com.ffreaky.shoppingservice.food.entity.FoodFoodCategoryEntity;
import com.ffreaky.shoppingservice.food.model.FoodCategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface FoodFoodCategoryRepository extends JpaRepository<FoodFoodCategoryEntity, Long> {

    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.FoodCategoryDto(
                ffc.id.foodId,
                fc.name
                )
            FROM FoodFoodCategoryEntity ffc
            JOIN FoodCategoryEntity fc
                ON ffc.id.foodCategoryId = fc.id
            WHERE ffc.id.foodId = :foodId
            """)
    Set<FoodCategoryDto> findAllFoodCategoryDtoByFoodId(Long foodId);

    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.FoodCategoryDto(
                ffc.id.foodId,
                fc.name
                )
            FROM FoodFoodCategoryEntity ffc
            JOIN FoodCategoryEntity fc
                ON ffc.id.foodCategoryId = fc.id
            """)
    Set<FoodCategoryDto> findAllFoodCategoryDto();
}
