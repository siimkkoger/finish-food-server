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
    public GetFoodOut getById(@PathVariable Long id, @Validated @RequestBody GetFoodsFilter filter) {
        return foodService.getFoodById(id, filter);
    }

    @PostMapping("/get-all")
    public List<GetFoodOut> getAllByFoodCategoryIds(@Validated @RequestBody GetFoodsFilter filter) {
        return foodService.getAll(filter);
    }

    @PostMapping("/create")
    public GetFoodOut createFood(@Validated @RequestBody CreateFoodRequestDto food) {
        return foodService.createFood(food);
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
