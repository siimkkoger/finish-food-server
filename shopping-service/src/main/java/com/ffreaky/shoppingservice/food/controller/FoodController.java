package com.ffreaky.shoppingservice.food.controller;

import com.ffreaky.shoppingservice.food.model.*;
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

    @PostMapping("/{id}")
    public GetFoodResponse getById(@PathVariable Long id, @Validated @RequestBody GetFoodRequestFilter filter) {
        return foodService.getFoodById(id, filter);
    }

    @PostMapping("/get-all")
    public List<GetFoodResponse> getAllByFoodCategoryIds(@Validated @RequestBody GetFoodRequestFilter filter) {
        return foodService.getAll(filter);
    }

    @PostMapping("/create")
    public GetFoodResponse createFood(@Validated @RequestBody CreateFoodRequest food) {
        return foodService.createFood(food);
    }

    @PutMapping("/{id}")
    public FoodDto updateFood(
            @PathVariable Long id,
            @Validated @RequestBody UpdateFoodRequest updatedFood) {
        return foodService.updateFood(id, updatedFood);
    }

    @DeleteMapping("/{id}")
    public void deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
    }

}
