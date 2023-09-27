package com.ffreaky.shoppingservice.food.repository;

import com.ffreaky.shoppingservice.food.entity.FoodCategoryEntity;
import com.ffreaky.shoppingservice.food.model.FoodCategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategoryEntity, Long> {

    @Query("SELECT fc FROM FoodCategoryEntity fc WHERE fc.id in :ids")
    Set<FoodCategoryEntity> findAllByIds(Set<Long> ids);

    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.FoodCategoryDto(
                ffc.id.foodId,
                fc.id,
                fc.name
                )
            FROM FoodFoodCategoryEntity ffc
            JOIN FoodCategoryEntity fc
                ON ffc.id.foodCategoryId = fc.id
            WHERE ffc.id.foodId = :foodId
            """)
    Set<FoodCategoryDto> findCategoriesForFoodById(Long foodId);

    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.FoodCategoryDto(
                ffc.id.foodId,
                fc.id,
                fc.name
                )
            FROM FoodFoodCategoryEntity ffc
            JOIN FoodCategoryEntity fc
                ON ffc.id.foodCategoryId = fc.id
            """)
    Set<FoodCategoryDto> findCategoriesForAllFoods();

}
