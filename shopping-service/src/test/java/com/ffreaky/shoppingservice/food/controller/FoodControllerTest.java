package com.ffreaky.shoppingservice.food.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffreaky.shoppingservice.food.model.FoodCategoryDto;
import com.ffreaky.shoppingservice.food.model.request.CreateFoodReqBody;
import com.ffreaky.shoppingservice.food.model.request.GetFoodsFilter;
import com.ffreaky.shoppingservice.food.model.request.UpdateFoodFoodCategoriesReqBody;
import com.ffreaky.shoppingservice.food.model.request.UpdateFoodReqBody;
import com.ffreaky.shoppingservice.food.model.response.GetFoodCategoryResponse;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import com.ffreaky.shoppingservice.food.service.FoodService;
import com.ffreaky.shoppingservice.product.ProductType;
import com.ffreaky.shoppingservice.product.model.request.CreateProductReqBody;
import com.ffreaky.shoppingservice.product.model.request.UpdateProductReqBody;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link FoodController}.
 * Testing the controller layer only using MockMvc.
 *
 *
 * <p>
 * "As of Spring Boot 2.1, we no longer need to load the SpringExtension
 * because it's included as a meta annotation in the Spring Boot test annotations
 * like @DataJpaTest, @WebMvcTest, and @SpringBootTest" - https://reflectoring.io/spring-boot-web-controller-test/
 * <p>
 *
 *
 * Learned:
 * that @Validated annotation does not work for validating fields of inner objects.
 * Use @Valid annotation instead.
 *
 */
@WebMvcTest(controllers = FoodController.class)
class FoodControllerTest {

    private final String controllerPath = "/food";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FoodService foodService;

    @Test
    public void testGetFoodById() throws Exception {
        // Arrange
        long foodId = 1L;
        final GetFoodResponse expectedResponse = new GetFoodResponse(
                foodId, "Sample Food", "Description", "image.jpg", "Restrictions",
                BigDecimal.valueOf(10.99), new Date(), ProductType.FOOD, "Provider");

        when(foodService.getFoodById(foodId)).thenReturn(expectedResponse);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/get-food/%s".formatted(foodId))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString());
    }

    @Test
    public void testGetFoodById_invalidInput() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/get-food/tere")
                        .contentType("application/json"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetFoods_invalidInput() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/get-foods")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new GetFoodsFilter(null)))
                        .contentType("application/json"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateFood() throws Exception {
        // Arrange
        var createProductReqBody = new CreateProductReqBody(
                ProductType.FOOD, 1L, "Sample Food",
                "Description", "image.jpg", BigDecimal.valueOf(10.99), new Date());

        var reqBody = new CreateFoodReqBody(createProductReqBody, "Restrictions");

        var expectedResponse = new GetFoodResponse(
                1L, "Sample Food", "Description", "image.jpg", "Restrictions",
                BigDecimal.valueOf(10.99), new Date(), ProductType.FOOD, "Provider");

        when(foodService.createFood(reqBody)).thenReturn(expectedResponse);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/create-food")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqBody))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateFood_invalidInput() {
        final List<CreateProductReqBody> invalidInputs = List.of(
                new CreateProductReqBody(null, 1L, "Sample Food",
                        "Description", "image.jpg", BigDecimal.valueOf(10.99), new Date()),
                new CreateProductReqBody(ProductType.FOOD, null, "Sample Food",
                        "Description", "image.jpg", BigDecimal.valueOf(10.99), new Date()),
                new CreateProductReqBody(ProductType.FOOD, 1L, null,
                        "Description", "image.jpg", BigDecimal.valueOf(10.99), new Date()),
                new CreateProductReqBody(ProductType.FOOD, 1L, "Sample Food",
                        "Description", "image.jpg", null, new Date()),
                new CreateProductReqBody(ProductType.FOOD, 1L, "Sample Food",
                        "Description", "image.jpg", BigDecimal.valueOf(-1L), new Date()),
                new CreateProductReqBody(ProductType.FOOD, 1L, "Sample Food",
                        "Description", "image.jpg", BigDecimal.valueOf(10.99), null)
        );

        invalidInputs.forEach(invalidInput -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders
                                .post(controllerPath + "/create-food")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new CreateFoodReqBody(invalidInput, "Restrictions")))
                                .contentType("application/json"))
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                        .andExpect(status().isBadRequest());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testUpdateFood() throws Exception {
        // Arrange
        var updateProductReqBody = new UpdateProductReqBody(
                "Sample Food", "Description", BigDecimal.valueOf(10.99), new Date());

        var reqBody = new UpdateFoodReqBody("Restrictions", updateProductReqBody);

        var expectedResponse = new GetFoodResponse(
                1L, "Sample Food", "Description", "image.jpg", "Restrictions",
                BigDecimal.valueOf(10.99), new Date(), ProductType.FOOD, "Provider");

        when(foodService.updateFood(1L, reqBody)).thenReturn(expectedResponse);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/update-food/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqBody))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString());
    }

    @Test
    public void testUpdateFood_invalidInput() throws Exception {
        // Test invalid PathVariable
        mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/create-food")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .contentType("application/json"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());

        // Test invalid UpdateFoodReqBody
        mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/update-food/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateFoodReqBody("Restrictions", null)))
                        .contentType("application/json"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());

        // Test invalid UpdateProductReqBody
        final var invalidInputs = List.of(
                new UpdateFoodReqBody("Restrictions",
                        new UpdateProductReqBody(null, "Description", BigDecimal.ONE, new Date())),
                new UpdateFoodReqBody("Restrictions",
                        new UpdateProductReqBody("Name", "Description", null, new Date())),
                new UpdateFoodReqBody("Restrictions",
                        new UpdateProductReqBody("Name", "Description", BigDecimal.valueOf(-1L), new Date())),
                new UpdateFoodReqBody("Restrictions",
                        new UpdateProductReqBody("Name", "Description", BigDecimal.ONE, null))
        );
        invalidInputs.forEach(invalidInput -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders
                                .post(controllerPath + "/update-food/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidInput))
                                .contentType("application/json"))
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                        .andExpect(status().isBadRequest());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testDeleteFood() throws Exception {
        // Arrange
        var expectedResponse = true;
        when(foodService.deleteFood(1L)).thenReturn(expectedResponse);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .delete(controllerPath + "/delete-food/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString());
    }

    @Test
    public void testDeleteFood_invalidInput() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(controllerPath + "/delete-food/tere")
                        .contentType("application/json"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateFoodCategoriesForFood() throws Exception {
        // Arrange
        var foodId = 1L;
        var reqBody = new UpdateFoodFoodCategoriesReqBody(Set.of(1L, 2L));
        var expectedResponse = new GetFoodCategoryResponse(Set.of(
                new FoodCategoryDto(1L, "Category 1"),
                new FoodCategoryDto(2L, "Category 2")));

        when(foodService.getAllFoodCategoriesForFood(foodId)).thenReturn(expectedResponse);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/update-food-categories-for-food/%s".formatted(foodId))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqBody))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString());
    }

    @Test
    public void testGetAllFoodCategories() throws Exception {
        // Arrange
        var expectedResponse = new GetFoodCategoryResponse(Set.of(
                new FoodCategoryDto(1L, "Category 1"),
                new FoodCategoryDto(2L, "Category 2")));

        when(foodService.getAllFoodCategories()).thenReturn(expectedResponse);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(controllerPath + "/get-food-categories")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString());
    }

    @Test
    public void testGetAllFoodCategoriesForFood() throws Exception {
        // Arrange
        var foodId = 1L;
        var expectedResponse = new GetFoodCategoryResponse(Set.of(
                new FoodCategoryDto(1L, "Category 1"),
                new FoodCategoryDto(2L, "Category 2")));

        when(foodService.getAllFoodCategoriesForFood(foodId)).thenReturn(expectedResponse);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(controllerPath + "/get-food-categories-for-food/%s".formatted(foodId))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        assertEquals(objectMapper.writeValueAsString(expectedResponse), result.getResponse().getContentAsString());
    }


}