package com.ffreaky.shoppingservice.food.controller;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.FoodDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/food")
public class FoodController {



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
