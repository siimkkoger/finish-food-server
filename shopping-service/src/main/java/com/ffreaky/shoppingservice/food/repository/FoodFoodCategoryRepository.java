package com.ffreaky.shoppingservice.food.repository;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.entity.FoodFoodCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface FoodFoodCategoryRepository extends JpaRepository<FoodFoodCategoryEntity, Long> {

    @Query("SELECT ffc FROM FoodFoodCategoryEntity ffc WHERE ffc.id.foodId = :foodId")
    Set<FoodFoodCategoryEntity> findAllByFoodId(Long foodId);

    @Query("SELECT f FROM FoodEntity f JOIN FoodFoodCategoryEntity ffc ON f.id = ffc.id.foodId WHERE ffc.id.foodCategoryId IN :foodCategoryIds")
    Set<FoodEntity> findFoodsByFoodCategoryIds(Set<Long> foodCategoryIds);

}
