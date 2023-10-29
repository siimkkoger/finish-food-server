package com.ffreaky.shoppingservice.food;

import java.util.List;

public class TestData {
    private List<FoodData> expectedFoods;

    public List<FoodData> getExpectedFoods() {
        return expectedFoods;
    }

    public void setExpectedFoods(List<FoodData> expectedFoods) {
        this.expectedFoods = expectedFoods;
    }

    public record FoodData(
            Long foodId,
            String foodName,
            String foodDescription,
            String foodImage,
            Boolean foodVegetarian,
            Boolean foodVegan,
            Boolean foodGlutenFree,
            Boolean foodNutFree,
            Boolean foodDairyFree,
            Boolean foodOrganic,
            String foodPrice,
            String foodPickupTime,
            String foodProductType,
            String foodProductProviderName
    ) {
    }
}