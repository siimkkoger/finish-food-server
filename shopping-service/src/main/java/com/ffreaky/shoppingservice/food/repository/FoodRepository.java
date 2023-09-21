package com.ffreaky.shoppingservice.food.repository;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.FoodDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
    
    public FoodDto findById(long id);
}
