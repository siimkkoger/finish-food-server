package com.ffreaky.shoppingservice.food.service;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.GetFoodResponseDto;
import com.ffreaky.shoppingservice.food.repository.FoodRepository;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public FoodEntity createFood(FoodEntity food) {
        return foodRepository.save(food);
    }

    public GetFoodResponseDto getFoodById(Long id) {
        GetFoodResponseDto food = foodRepository.findByIdCustom(id);
        if (food != null) {
            return food;
        } else {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id);
        }
    }

    public List<GetFoodResponseDto> getAllFoods() {
        return foodRepository.findAllCustom();
    }

    public FoodEntity updateFood(Long id, FoodEntity updatedFood) {
        if (foodRepository.existsById(id)) {
            updatedFood.setId(id);
            return foodRepository.save(updatedFood);
        } else {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id);
        }
    }

    public void deleteFood(Long id) {
        if (foodRepository.existsById(id)) {
            foodRepository.deleteById(id);
        } else {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id);
        }
    }
}
