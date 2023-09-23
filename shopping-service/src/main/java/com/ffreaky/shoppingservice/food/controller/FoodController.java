package com.ffreaky.shoppingservice.food.controller;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.FoodDto;
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
    public FoodEntity getFoodById(@PathVariable Long id) {
        return foodService.getFoodById(id);
    }

    @GetMapping
    public List<FoodEntity> getAllFoods() {
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

    public FoodDto foodEntityToDto(FoodEntity food) {
        return new FoodDto(
                food.getId(),
                food.getName(),
                food.getDescription(),
                food.getImage(),
                food.getDietaryRestrictions()
        );
    }

    public FoodEntity foodDtoToEntity(FoodDto foodDto) {
        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setId(foodDto.id());
        foodEntity.setName(foodDto.name());
        foodEntity.setDescription(foodDto.description());
        foodEntity.setImage(foodDto.image());
        foodEntity.setDietaryRestrictions(foodDto.dietaryRestrictions());
        return foodEntity;
    }

}
