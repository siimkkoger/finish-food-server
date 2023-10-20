package com.ffreaky.shoppingservice.food.controller;

import com.ffreaky.shoppingservice.food.model.FoodCategoryDto;
import com.ffreaky.shoppingservice.food.model.request.GetFoodReqBody;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import com.ffreaky.shoppingservice.food.service.FoodService;
import com.ffreaky.shoppingservice.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FoodControllerTest {

    @Mock
    private FoodService foodService;

    private FoodController foodController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        foodController = new FoodController(foodService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testGetFoodById() {
        Long foodId = 1L;

        // Test with and without food categories
        GetFoodReqBody req_includeCategories = new GetFoodReqBody(foodId, true);
        GetFoodReqBody req_noCategories = new GetFoodReqBody(foodId, false);

        GetFoodResponse expectedResponse_withCategories = new GetFoodResponse(
                1L, "Sample Food", "Description", "image.jpg", "Restrictions",
                BigDecimal.valueOf(10.99), new Date(), ProductType.FOOD,
                "Provider", Set.of(new FoodCategoryDto(1L, "Category1"))
        );
        GetFoodResponse expectedResponse_withoutCategories = new GetFoodResponse(
                1L, "Sample Food", "Description", "image.jpg", "Restrictions",
                BigDecimal.valueOf(10.99), new Date(), ProductType.FOOD,
                "Provider", Set.of(new FoodCategoryDto(1L, "Category1"))
        );

        // Mock the behavior of foodService.getFoodById()
        when(foodService.getFoodById(req_includeCategories.foodId(), req_includeCategories.includeFoodCategories())).thenReturn(expectedResponse_withCategories);
        when(foodService.getFoodById(req_includeCategories.foodId(), req_noCategories.includeFoodCategories())).thenReturn(expectedResponse_withoutCategories);

        // Call the controller method
        GetFoodResponse actualResponse_withCategories = foodController.getFoodById(req_includeCategories);
        GetFoodResponse actualResponse_withoutCategories = foodController.getFoodById(req_noCategories);

        // Assert that the actual response matches the expected response
        assertEquals(expectedResponse_withCategories, actualResponse_withCategories);
        assertEquals(expectedResponse_withoutCategories, actualResponse_withoutCategories);
    }

    @Test
    void getAllFoodsByFoodCategoryIds() {
    }

    @Test
    void createFood() {
    }

    @Test
    void updateFood() {
    }

    @Test
    void deleteFood() {
    }

    @Test
    void updateFoodCategory() {
    }

    @Test
    void getAllFoodCategories() {
    }

    @Test
    void getAllFoodCategoriesForFood() {
    }
}