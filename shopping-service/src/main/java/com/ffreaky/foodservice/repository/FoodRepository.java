package com.ffreaky.foodservice.repository;

import com.ffreaky.foodservice.entity.FoodEntity;
import com.ffreaky.foodservice.model.FoodDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
    
    public FoodDto findById(long id);
}
