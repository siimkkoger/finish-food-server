package com.ffreaky.foodservice.controller;

import com.ffreaky.foodservice.entity.FoodEntity;
import com.ffreaky.foodservice.model.FoodDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/food")
public class FoodController {

    @PostMapping(value = "/get")
    FoodDto getFood(long id) {
        return new FoodDto(id, "name", null);
    }

    @PostMapping(value = "/create")
    boolean createFood(@Valid @RequestBody FoodDto foodDto) {
        System.out.println(foodDto);
        return true;
    }

    public FoodDto foodEntityToDto(FoodEntity food) {
        return new FoodDto(food.getId(), food.getName(), food.getPrice());
    }

    public FoodEntity foodDtoToEntity(FoodDto foodDto) {
        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setId(foodDto.id());
        foodEntity.setName(foodDto.name());
        foodEntity.setPrice(foodDto.price());
        return foodEntity;
    }

}
