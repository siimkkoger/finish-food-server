package com.example.finishfoodserver.controller;

import com.example.finishfoodserver.model.FoodDTO;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/food")
public class FoodController {

    @PostMapping(value = "/get")
    FoodDTO getFood(long id) {
        return FoodDTO.builder()
                .id(id)
                .name("Food name")
                .price(BigDecimal.valueOf(100))
                .build();
    }

    @PostMapping(value = "/create")
    boolean createFood(@RequestBody FoodDTO foodDTO) {
        System.out.println(foodDTO);
        return true;
    }


}
