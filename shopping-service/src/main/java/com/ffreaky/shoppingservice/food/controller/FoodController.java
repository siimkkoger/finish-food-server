package com.ffreaky.shoppingservice.food.controller;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.GetFoodResponseDto;
import com.ffreaky.shoppingservice.food.service.FoodService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/food")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping
    public FoodEntity createFood(@Validated @RequestBody FoodEntity food) {
        return foodService.createFood(food);
    }

    @GetMapping("/{id}")
    public GetFoodResponseDto getFoodById(@PathVariable Long id) {
        return foodService.getFoodById(id);
    }

    @GetMapping
    public List<GetFoodResponseDto> getAllFoods() {
        return foodService.getAllFoods();
    }

    @PutMapping("/{id}")
    public FoodEntity updateFood(
            @PathVariable Long id,
            @Validated @RequestBody FoodEntity updatedFood) {
        return foodService.updateFood(id, updatedFood);
    }

    @DeleteMapping("/{id}")
    public void deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
    }

}
