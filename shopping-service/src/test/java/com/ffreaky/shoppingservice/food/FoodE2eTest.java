package com.ffreaky.shoppingservice.food;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.request.GetFoodsFilter;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import com.ffreaky.shoppingservice.food.repository.FoodFoodCategoryRepository;
import com.ffreaky.shoppingservice.product.ProductOrderBy;
import com.ffreaky.shoppingservice.product.ProductType;
import com.querydsl.core.types.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private FoodFoodCategoryRepository foodFoodCategoryRepository;

    private final String controllerPath = "/food";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void test() {
        assertThat(applicationName).isEqualTo("shopping-service-test");
    }

    @Test
    void testGetFoodById() throws Exception {
        // Given
        var expectedResponse = getUnfilteredResponse();
        var expectedFood_id1 = expectedResponse.get(0);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/get-food/%s".formatted(expectedFood_id1.id()))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedFood_id1));
    }

    @Test
    void testGetFoods() throws Exception {
        // Given
        var expectedResponse = getUnfilteredResponse();
        var filter = new GetFoodsFilter(null, null, null, null, null, null, null, null);

        // When
        MvcResult result = createMockRequestGetFoods(filter);

        // Then
        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponse));
    }

    @Test
    void testGetFoods_pagination() throws Exception {
        // Given
        var expectedResponse = getUnfilteredResponse();

        // When
        // page = 1 and pageSize = 10
        var filter = new GetFoodsFilter(null, null, null, null, 1, 10, ProductOrderBy.ID, Order.ASC);
        MvcResult result_10 = createMockRequestGetFoods(filter);
        // Then page 1 should return 10 items
        assertThat(result_10.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponse));

        // When
        // page = 1 and pageSize = 1
        filter = new GetFoodsFilter(null, null, null, null, 1, 5, ProductOrderBy.ID, Order.ASC);
        MvcResult result_5 = createMockRequestGetFoods(filter);
        // Then page 1 should return 5 items
        assertThat(result_5.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponse.subList(0, 5)));

        // When
        // page = 2 and pageSize = 5
        filter = new GetFoodsFilter(null, null, null, null, 2, 5, ProductOrderBy.ID, Order.ASC);
        MvcResult result_10_page_2 = createMockRequestGetFoods(filter);
        // Then page 2 should return foods 5 - 10
        var expectedResponseFromFifthToTenth = expectedResponse.subList(5, 10);
        assertThat(result_10_page_2.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponseFromFifthToTenth));
    }

    @Test
    void testGetFoods_sorting() throws Exception {
        // Given
        var expectedResponse = getUnfilteredResponse();

        var page = 1;
        var pageSize = 10;
        var order = Order.ASC;

        // When
        // order ASC
        var filter = new GetFoodsFilter(null, null, null, null, page, pageSize, ProductOrderBy.ID, order);
        MvcResult result_price_asc = createMockRequestGetFoods(filter);
        // Then
        assertThat(result_price_asc.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponse));

        // When
        // OrderBy = NAME
        filter = new GetFoodsFilter(null, null, null, null, page, pageSize, ProductOrderBy.NAME, order);
        MvcResult result_name_asc = createMockRequestGetFoods(filter);
        // Then
        var expectedResponseSortedByName = expectedResponse.stream().sorted(Comparator.comparing(GetFoodResponse::name)).toList();
        assertThat(result_name_asc.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponseSortedByName));

        // When
        // OrderBy = PRICE
        filter = new GetFoodsFilter(null, null, null, null, page, pageSize, ProductOrderBy.PRICE, order);
        MvcResult result_price_asc_2 = createMockRequestGetFoods(filter);
        // Then
        var expectedResponseSortedByPrice = expectedResponse.stream().sorted(Comparator.comparing(GetFoodResponse::price)).toList();
        assertThat(result_price_asc_2.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponseSortedByPrice));

        // When
        // OrderBy = PICKUP_TIME
        filter = new GetFoodsFilter(null, null, null, null, page, pageSize, ProductOrderBy.PICKUP_TIME, order);
        MvcResult result_pickup_time_asc = createMockRequestGetFoods(filter);
        // Then
        var expectedResponseSortedByPickupTime = expectedResponse.stream().sorted(Comparator.comparing(GetFoodResponse::pickupTime)).toList();
        assertThat(result_pickup_time_asc.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponseSortedByPickupTime));

        // When
        // OrderBy = CREATED_AT
        filter = new GetFoodsFilter(null, null, null, null, page, pageSize, ProductOrderBy.CREATED_AT, order);
        MvcResult result_created_at_asc = createMockRequestGetFoods(filter);
        // Then (id and createdAt order are the same in db test data script)
        assertThat(result_created_at_asc.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponse));

        // When
        // OrderBy DESC
        order = Order.DESC;
        filter = new GetFoodsFilter(null, null, null, null, page, pageSize, ProductOrderBy.ID, order);
        MvcResult result_price_desc = createMockRequestGetFoods(filter);
        // Then
        var expectedResponseReversed = expectedResponse.stream().sorted((f1, f2) -> f2.id().compareTo(f1.id())).toList();
        assertThat(result_price_desc.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponseReversed));
    }

    @Test
    void testGetFoods_by_foodCategoryIds() throws Exception {
        // Given
        var expectedResponse = getUnfilteredResponse();

        Set<Long> categoryIds = Set.of(1L, 3L);
        Set<FoodEntity> foodEntities = foodFoodCategoryRepository.findFoodsByFoodCategoryIds(categoryIds);
        expectedResponse.removeIf(exampleFood -> foodEntities.stream().noneMatch(foodEntity -> foodEntity.getId().equals(exampleFood.id())));

        // When
        var filter = new GetFoodsFilter(categoryIds, null, null, null, null, null, null, null);
        MvcResult result = createMockRequestGetFoods(filter);

        // Then
        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponse));
    }

    /*
    should ignore case
     */
    @Test
    void testGetFoods_by_productProviderName() throws Exception {
        // Given
        var expectedResponse = getUnfilteredResponse();

        String productProviderSearch = "mc";
        expectedResponse.removeIf(exampleFood -> !exampleFood.productProviderName().toLowerCase().contains(productProviderSearch));

        // When
        var filter = new GetFoodsFilter(null, productProviderSearch, null, null, null, null, null, null);
        MvcResult result = createMockRequestGetFoods(filter);

        // Then
        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponse));
        assertThat(expectedResponse.size()).isEqualTo(7);
    }

    @Test
    void testGetFoods_by_pickupTime() throws Exception {
        // Given
        var expectedResponse = getUnfilteredResponse();

        final var pickupTimeFrom = LocalDateTime.parse("2023-09-22 12:00:01", formatter);
        expectedResponse.removeIf(exampleFood -> exampleFood.pickupTime().isBefore(pickupTimeFrom));
        final var pickupTimeTo = LocalDateTime.parse("2023-09-22 13:29:59", formatter);
        expectedResponse.removeIf(exampleFood -> exampleFood.pickupTime().isAfter(pickupTimeTo));

        // When
        var filter = new GetFoodsFilter(null, null, pickupTimeFrom, pickupTimeTo, null, null, null, null);
        MvcResult result = createMockRequestGetFoods(filter);

        // Then
        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponse));
        assertThat(expectedResponse.size()).isEqualTo(0);

        // Given
        expectedResponse = getUnfilteredResponse();

        final var pickupTimeFrom2 = LocalDateTime.parse("2023-09-22 12:00:00", formatter);
        expectedResponse.removeIf(exampleFood -> exampleFood.pickupTime().isBefore(pickupTimeFrom2));
        final var pickupTimeTo2 = LocalDateTime.parse("2023-09-22 13:30:00", formatter);
        expectedResponse.removeIf(exampleFood -> exampleFood.pickupTime().isAfter(pickupTimeTo2));

        // When
        filter = new GetFoodsFilter(null, null, pickupTimeFrom2, pickupTimeTo2, null, null, null, null);
        result = createMockRequestGetFoods(filter);

        // Then
        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponse));
        assertThat(expectedResponse.size()).isEqualTo(2);
    }

    private MvcResult createMockRequestGetFoods(GetFoodsFilter filter) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/get-foods")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
    }

    private List<GetFoodResponse> getUnfilteredResponse() throws IOException {
        var resource = new ClassPathResource("test-data.json");
        var testData = objectMapper.readValue(resource.getFile(), TestData.class);
        var expectedFoods = testData.getExpectedFoods();
        return expectedFoods.stream()
                .map(exampleFood -> new GetFoodResponse(
                        exampleFood.foodId(),
                        exampleFood.foodName(),
                        exampleFood.foodDescription(),
                        exampleFood.foodImage(),
                        exampleFood.foodDietaryRestrictions(),
                        new BigDecimal(exampleFood.foodPrice()),
                        LocalDateTime.parse(exampleFood.foodPickupTime(), formatter),
                        ProductType.FOOD,
                        exampleFood.foodProductProviderName()
                ))
                .collect(Collectors.toList());
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
