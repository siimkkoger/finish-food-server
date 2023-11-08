package com.ffreaky.shoppingservice.food.controller;

import com.ffreaky.shoppingservice.food.model.FoodCategoryDto;
import com.ffreaky.shoppingservice.food.model.request.*;
import com.ffreaky.shoppingservice.food.model.response.GetFoodListResult;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import com.ffreaky.shoppingservice.food.service.FoodService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "api/food")
public class FoodController {

    static Logger logger = LoggerFactory.getLogger(FoodController.class);

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/get-food/{foodId}")
    public GetFoodResponse getFoodById(@PathVariable @NotNull Long foodId) {
        return foodService.getFoodById(foodId);
    }

    @PostMapping("/add-food-to-cart/{foodId}")
    public boolean addFoodToCart(@PathVariable @NotNull Long foodId) {
        return foodService.foodExists(foodId);
    }

    @PostMapping("/get-foods")
    public GetFoodListResult getFoods(@Valid @RequestBody GetFoodsFilter filter) {
        try {
            Thread.sleep(1 * 1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        return foodService.getFoods(filter);
    }

    @PostMapping("/create-food")
    public GetFoodResponse createFood(@Valid @RequestBody CreateFoodReqBody food) {
        return foodService.createFood(food);
    }

    // TODO - support validation groups
    @PostMapping("/update-food/{foodId}")
    public GetFoodResponse updateFood(
            @PathVariable @NotNull Long foodId,
            @Valid @RequestBody UpdateFoodReqBody updatedFood) {
        return foodService.updateFood(foodId, updatedFood);
    }

    @DeleteMapping("/delete-food/{foodId}")
    public boolean deleteFood(@PathVariable @NotNull Long foodId) {
        return foodService.deleteFood(foodId);
    }

    /**
     * Update the food categories for food.
     * If a food in database has a food category that is not in the request body, it will be deleted.
     */
    @PostMapping("/update-food-categories-for-food/{foodId}")
    public Set<FoodCategoryDto> updateFoodFoodCategories(
            @PathVariable @NotNull Long foodId,
            @Valid @RequestBody UpdateFoodFoodCategoriesReqBody req) {
        foodService.createOrUpdateFoodFoodCategories(foodId, req);
        return foodService.getAllFoodCategoriesForFood(foodId);
    }

    @GetMapping("/get-food-categories")
    public Set<FoodCategoryDto> getAllFoodCategories() {
        return foodService.getAllFoodCategories();
    }

    @GetMapping("/get-food-categories-for-food/{foodId}")
    public Set<FoodCategoryDto> getAllFoodCategoriesForFood(@PathVariable @NotNull Long foodId) {
        return foodService.getAllFoodCategoriesForFood(foodId);
    }

}
