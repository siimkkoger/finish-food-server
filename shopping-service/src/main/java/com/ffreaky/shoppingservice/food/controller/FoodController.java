package com.ffreaky.shoppingservice.food.controller;

import com.ffreaky.shoppingservice.food.model.CreateFoodRequestDto;
import com.ffreaky.shoppingservice.food.model.FoodDto;
import com.ffreaky.shoppingservice.food.model.GetFoodOut;
import com.ffreaky.shoppingservice.food.model.UpdateFoodRequestDto;
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
    public FoodDto createFood(@Validated @RequestBody CreateFoodRequestDto food) {
        return foodService.createFood(food);
    }

    @GetMapping("/{id}")
    public GetFoodOut getFoodById(@PathVariable Long id) {
        return foodService.getFoodById(id);
    }

    @GetMapping
    public List<GetFoodOut> getAllFoods() {
        return foodService.getAllFoods();
    }

    @PutMapping("/{id}")
    public FoodDto updateFood(
            @PathVariable Long id,
            @Validated @RequestBody UpdateFoodRequestDto updatedFood) {
        return foodService.updateFood(id, updatedFood);
    }

    @DeleteMapping("/{id}")
    public void deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
    }

}
