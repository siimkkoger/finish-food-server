package com.ffreaky.shoppingservice.food.controller;

import com.ffreaky.shoppingservice.food.model.request.*;
import com.ffreaky.shoppingservice.food.model.response.GetFoodCategoryResponse;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
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

    @PostMapping("/get-food/{foodId}")
    public GetFoodResponse getFoodById(@Validated @RequestBody GetFoodReqBody req) {
        return foodService.getFoodById(req.foodId(), req.includeFoodCategories());
    }

    @PostMapping("/get-foods")
    public List<GetFoodResponse> getFoods(@Validated @RequestBody GetFoodsFilter filter) {
        return foodService.getFoods(filter);
    }

    @PostMapping("/create-food")
    public GetFoodResponse createFood(@Validated @RequestBody CreateFoodReqBody food) {
        return foodService.createFood(food);
    }

    @PostMapping("/update-food")
    public GetFoodResponse updateFood(@Validated @RequestBody UpdateFoodReqBody updatedFood) {
        return foodService.updateFood(updatedFood);
    }

    @DeleteMapping("/delete-food/{foodId}")
    public boolean deleteFood(@PathVariable Long foodId) {
        return foodService.deleteFood(foodId);
    }

    @PostMapping("/update-food-category")
    public GetFoodResponse updateFoodFoodCategories(@Validated @RequestBody UpdateFoodFoodCategoriesReqBody req) {
        foodService.createOrUpdateFoodFoodCategories(req);
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
