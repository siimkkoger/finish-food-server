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
    public GetFoodResponse getById(@PathVariable Long id, @Validated @RequestBody FoodFilter filter) {
        return foodService.getFoodById(id, filter.includeFoodCategories());
    }

    @PostMapping("/get-all")
    public List<GetFoodResponse> getAllByFoodCategoryIds(@Validated @RequestBody FoodFilter filter) {
        return foodService.getAll(filter);
    }

    @PostMapping("/create")
    public GetFoodResponse createFood(@Validated @RequestBody CreateFoodRequest food) {
        return foodService.createFood(food);
    }

    @PostMapping("/update")
    public GetFoodResponse updateFood(@Validated @RequestBody UpdateFoodRequest updatedFood) {
        return foodService.updateFood(updatedFood);
    }

    @PostMapping("/update-food-category")
    public GetFoodResponse updateFoodCategory(@Validated @RequestBody UpdateFoodCategoryRequest req) {
        foodService.createOrUpdateFoodCategories(req);
        return foodService.getFoodById(req.foodId(), true);
    }

    @DeleteMapping("/{id}")
    public void deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
    }

    @GetMapping("/get-food-categories")
    public List<GetFoodCategoryResponse> getAllFoodCategories() {
        return foodService.getAllFoodCategories();
    }

}
