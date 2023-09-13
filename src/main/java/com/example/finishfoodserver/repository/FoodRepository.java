package com.example.finishfoodserver.repository;

import com.example.finishfoodserver.entity.FoodEntity;
import com.example.finishfoodserver.model.FoodDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
    
    public FoodDto findById(long id);
}
