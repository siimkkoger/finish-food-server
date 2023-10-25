package com.ffreaky.shoppingservice.food;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffreaky.shoppingservice.food.controller.FoodController;
import com.ffreaky.shoppingservice.food.model.request.CreateFoodReqBody;
import com.ffreaky.shoppingservice.food.model.request.GetFoodsFilter;
import com.ffreaky.shoppingservice.food.model.request.UpdateFoodFoodCategoriesReqBody;
import com.ffreaky.shoppingservice.food.model.request.UpdateFoodReqBody;
import com.ffreaky.shoppingservice.food.model.response.GetFoodCategoryResponse;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import com.ffreaky.shoppingservice.product.ProductType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-test.properties")
@AutoConfigureMockMvc
public class FoodE2eTest {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String controllerPath = "/food";

    @Test
    void test() {
        assertThat(applicationName).isEqualTo("shopping-service-test");
    }

    @Test
    void testGetFoodById() throws Exception {
        var resource = new ClassPathResource("test-data.json");
        var testData = objectMapper.readValue(resource.getFile(), TestData.class);
        var expectedFoods = testData.getExpectedFoods();
        var expectedFood_id1 = expectedFoods.get(0);

        // Given
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var expectedResponse = new GetFoodResponse(
                expectedFood_id1.foodId(),
                expectedFood_id1.foodName(),
                expectedFood_id1.foodDescription(),
                expectedFood_id1.foodImage(),
                expectedFood_id1.foodDietaryRestrictions(),
                new BigDecimal(expectedFood_id1.foodPrice()),
                LocalDateTime.parse(expectedFood_id1.foodPickupTime(), formatter),
                ProductType.FOOD,
                expectedFood_id1.foodProductProviderName()
        );

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/get-food/%s".formatted(expectedFood_id1.foodId()))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    void testGetFoods() {
        // Given
        GetFoodsFilter filter = new GetFoodsFilter();
        filter.setName("Pizza");

        // When
        List<GetFoodResponse> getFoodResponses = foodController.getFoods(filter);

        // Then
        assertThat(getFoodResponses).isNotNull();
        assertThat(getFoodResponses.size()).isGreaterThan(0);
    }


    /*

    @Test
    void testCreateFood() {
        // Given
        CreateFoodReqBody createFoodReqBody = new CreateFoodReqBody();
        createFoodReqBody.setName("Hamburger");
        createFoodReqBody.setDescription("A delicious hamburger with beef, cheese, lettuce, tomato, and onion.");
        createFoodReqBody.setImage("https://example.com/hamburger.jpg");
        createFoodReqBody.setPrice(BigDecimal.valueOf(10.99));
        createFoodReqBody.setPickupTime(LocalDateTime.now().plusHours(1));

        // When
        GetFoodResponse getFoodResponse = foodController.createFood(createFoodReqBody);

        // Then
        assertThat(getFoodResponse).isNotNull();
        assertThat(getFoodResponse.getName()).isEqualTo(createFoodReqBody.getName());
        assertThat(getFoodResponse.getDescription()).isEqualTo(createFoodReqBody.getDescription());
        assertThat(getFoodResponse.getImage()).isEqualTo(createFoodReqBody.getImage());
        assertThat(getFoodResponse.getPrice()).isEqualTo(createFoodReqBody.getPrice());
        assertThat(getFoodResponse.getPickupTime()).isEqualTo(createFoodReqBody.getPickupTime());
    }

    @Test
    void testUpdateFood() {
        // Given
        Long foodId = 1L;
        UpdateFoodReqBody updateFoodReqBody = new UpdateFoodReqBody();
        updateFoodReqBody.setName("Cheeseburger");
        updateFoodReqBody.setDescription("A delicious cheeseburger with beef, cheese, lettuce, tomato, onion, and ketchup.");

        // When
        GetFoodResponse getFoodResponse = foodController.updateFood(foodId, updateFoodReqBody);

        // Then
        assertThat(getFoodResponse).isNotNull();
        assertThat(getFoodResponse.getName()).isEqualTo(updateFoodReqBody.getName());
        assertThat(getFoodResponse.getDescription()).isEqualTo(updateFoodReqBody.getDescription());
    }

    @Test
    void testDeleteFood() {
        // Given
        Long foodId = 1L;

        // When
        boolean success = foodController.deleteFood(foodId);

        // Then
        assertThat(success).isTrue();
    }

    @Test
    void testUpdateFoodFoodCategories() {
        // Given
        Long foodId = 1L;
        UpdateFoodFoodCategoriesReqBody updateFoodFoodCategoriesReqBody = new UpdateFoodFoodCategoriesReqBody();
        updateFoodFoodCategoriesReqBody.setFoodCategories(List.of(1L, 2L));

        // When
        GetFoodCategoryResponse getFoodCategoryResponse = foodController.updateFoodFoodCategories(foodId, updateFoodFoodCategoriesReqBody);

        // Then
        assertThat(getFoodCategoryResponse).isNotNull();
        assertThat(getFoodCategoryResponse.getFoodCategories().size()).isEqualTo(2);
    }

    @Test
    void testGetAllFoodCategories() {
        // Given

        // When
        GetFoodCategoryResponse getFoodCategoryResponse = foodController.getAllFoodCategories();

        // Then
        assertThat(getFoodCategoryResponse).isNotNull();
        assertThat(getFoodCategoryResponse.getFoodCategories().size()).isGreaterThan(0);
    }

    @Test
    void testGetAllFoodCategories() {
        // Given

        // When
        GetFoodCategoryResponse getFoodCategoryResponse = foodController.getAllFoodCategories();

        // Then
        assertThat(getFoodCategoryResponse).isNotNull();
        assertThat(getFoodCategoryResponse.getFoodCategories().size()).isGreaterThan(0);
    }

    @Test
    void testGetAllFoodCategoriesForFood() {
        // Given
        Long foodId = 1L;

        // When
        GetFoodCategoryResponse getFoodCategoryResponse = foodController.getAllFoodCategoriesForFood(foodId);

        // Then
        assertThat(getFoodCategoryResponse).isNotNull();
        assertThat(getFoodCategoryResponse.getFoodCategories().size()).isGreaterThan(0);
    }
     */

}
