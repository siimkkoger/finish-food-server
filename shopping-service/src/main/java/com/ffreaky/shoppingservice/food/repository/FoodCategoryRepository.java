package com.ffreaky.shoppingservice.food.repository;

import com.ffreaky.shoppingservice.food.entity.FoodCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategoryEntity, Long> {

    @Query("SELECT f FROM FoodCategoryEntity f WHERE f.id in :ids")
    Set<FoodCategoryEntity> findAllByIds(Set<Long> ids);

    Set<FoodCategoryEntity> findAllByFoodsId(Long id);

}
