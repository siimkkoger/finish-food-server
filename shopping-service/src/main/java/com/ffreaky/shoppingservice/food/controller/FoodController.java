package com.ffreaky.shoppingservice.food.controller;

import com.ffreaky.shoppingservice.food.model.request.*;
import com.ffreaky.shoppingservice.food.model.response.GetFoodCategoryResponse;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import com.ffreaky.shoppingservice.food.service.FoodService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    public GetFoodResponse getFoodById(@PathVariable @NotNull Long foodId) {
        return foodService.getFoodById(foodId);
    }

    @PostMapping("/get-foods")
    public List<GetFoodResponse> getFoods(@Validated @RequestBody GetFoodsFilter filter) {
        return foodService.getFoods(filter);
    }

    @PostMapping("/create-food")
    public GetFoodResponse createFood(@Validated @RequestBody CreateFoodReqBody food) {
        return foodService.createFood(food);
    }

    @PostMapping("/update-food/{foodId}")
    public GetFoodResponse updateFood(
            @PathVariable @NotNull Long foodId,
            @Validated @RequestBody UpdateFoodReqBody updatedFood) {
        return foodService.updateFood(foodId, updatedFood);
    }

    @DeleteMapping("/delete-food/{foodId}")
    public boolean deleteFood(@PathVariable @NotNull Long foodId) {
        return foodService.deleteFood(foodId);
    }

    @PostMapping("/update-food-categories-for-food")
    public GetFoodCategoryResponse updateFoodFoodCategories(@Validated @RequestBody UpdateFoodFoodCategoriesReqBody req) {
        foodService.createOrUpdateFoodFoodCategories(req);
        return foodService.getAllFoodCategoriesForFood(req.foodId());
    }

    @GetMapping("/get-food-categories")
    public GetFoodCategoryResponse getAllFoodCategories() {
        return foodService.getAllFoodCategories();
    }

    @GetMapping("/get-food-categories-for-food/{foodId}")
    public GetFoodCategoryResponse getAllFoodCategoriesForFood(@PathVariable @NotNull Long foodId) {
        return foodService.getAllFoodCategoriesForFood(foodId);
    }

}
