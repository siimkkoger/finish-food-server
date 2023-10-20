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

    @PostMapping("/{foodId}")
    public GetFoodResponse getFoodById(@PathVariable Long foodId, @Validated @RequestBody FoodFilter filter) {
        return foodService.getFoodById(foodId, filter.includeFoodCategories());
    }

    @PostMapping("/get-all")
    public List<GetFoodResponse> getAllFoodsByFoodCategoryIds(@Validated @RequestBody FoodFilter filter) {
        return foodService.getFoods(filter);
    }

    @PostMapping("/create")
    public GetFoodResponse createFood(@Validated @RequestBody CreateFoodRequest food) {
        return foodService.createFood(food);
    }

    @PostMapping("/update")
    public GetFoodResponse updateFood(@Validated @RequestBody UpdateFoodRequest updatedFood) {
        return foodService.updateFood(updatedFood);
    }

    @DeleteMapping("/{foodId}")
    public boolean deleteFood(@PathVariable Long foodId) {
        return foodService.deleteFood(foodId);
    }

    @PostMapping("/update-food-category")
    public GetFoodResponse updateFoodCategory(@Validated @RequestBody UpdateFoodCategoryRequest req) {
        foodService.createOrUpdateFoodCategories(req);
        return foodService.getFoodById(req.foodId(), true);
    }

    @GetMapping("/get-food-categories")
    public GetFoodCategoryResponse getAllFoodCategories() {
        return foodService.getAllFoodCategories();
    }

    @GetMapping("/get-food-categories-for-food/{foodId}")
    public GetFoodCategoryResponse getAllFoodCategoriesForFood(@PathVariable Long foodId) {
        return foodService.getAllFoodCategoriesForFood(foodId);
    }

}
