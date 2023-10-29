package com.ffreaky.shoppingservice.food;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.request.CreateFoodReqBody;
import com.ffreaky.shoppingservice.food.model.request.GetFoodsFilter;
import com.ffreaky.shoppingservice.food.model.request.UpdateFoodReqBody;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import com.ffreaky.shoppingservice.food.repository.FoodFoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodRepository;
import com.ffreaky.shoppingservice.product.ProductOrderBy;
import com.ffreaky.shoppingservice.product.ProductType;
import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import com.ffreaky.shoppingservice.product.model.request.CreateProductReqBody;
import com.ffreaky.shoppingservice.product.model.request.UpdateProductReqBody;
import com.ffreaky.shoppingservice.product.repository.ProductRepository;
import com.querydsl.core.types.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
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
@Sql(scripts = {"/migration/setup-test-schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/migration/teardown-test-schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class FoodE2eTest {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FoodFoodCategoryRepository foodFoodCategoryRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ProductRepository productRepository;

    private final String controllerPath = "/api/food";
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
                        .get(controllerPath + "/get-food/%s".formatted(expectedFood_id1.foodId()))
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
        // Then (foodId and createdAt order are the same in db test data script)
        assertThat(result_created_at_asc.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponse));

        // When
        // OrderBy DESC
        order = Order.DESC;
        filter = new GetFoodsFilter(null, null, null, null, page, pageSize, ProductOrderBy.ID, order);
        MvcResult result_price_desc = createMockRequestGetFoods(filter);
        // Then
        var expectedResponseReversed = expectedResponse.stream().sorted((f1, f2) -> f2.foodId().compareTo(f1.foodId())).toList();
        assertThat(result_price_desc.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponseReversed));
    }

    @Test
    void testGetFoods_by_foodCategoryIds() throws Exception {
        // Given
        var expectedResponse = getUnfilteredResponse();

        Set<Long> categoryIds = Set.of(1L, 3L);
        Set<FoodEntity> foodEntities = foodFoodCategoryRepository.findFoodsByFoodCategoryIds(categoryIds);
        expectedResponse.removeIf(exampleFood -> foodEntities.stream().noneMatch(foodEntity -> foodEntity.getId().equals(exampleFood.foodId())));

        // When
        var filter = new GetFoodsFilter(categoryIds, null, null, null, null, null, null, null);
        MvcResult result = createMockRequestGetFoods(filter);

        // Then
        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedResponse));
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
                        exampleFood.foodVegetarian(),
                        exampleFood.foodVegan(),
                        exampleFood.foodGlutenFree(),
                        exampleFood.foodNutFree(),
                        exampleFood.foodDairyFree(),
                        exampleFood.foodOrganic(),
                        new BigDecimal(exampleFood.foodPrice()),
                        LocalDateTime.parse(exampleFood.foodPickupTime(), formatter),
                        ProductType.FOOD,
                        exampleFood.foodProductProviderName()
                ))
                .collect(Collectors.toList());
    }

    @Test
    void testCreateFood() throws Exception {
        // Given
        var createProductReqBody = new CreateProductReqBody(
                ProductType.FOOD,
                1L, // McDonalds
                "New Created Food",
                "New Created Food Description",
                "https://example.com/new-created-food.jpg",
                new BigDecimal("9.9900"),
                LocalDateTime.now().plusHours(1).withNano(0)
        );
        var createFoodReqBody = new CreateFoodReqBody(createProductReqBody, false, false, false, false, false, false);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/create-food")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFoodReqBody))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        var response = objectMapper.readValue(result.getResponse().getContentAsString(), GetFoodResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo(createProductReqBody.name());
        assertThat(response.description()).isEqualTo(createProductReqBody.description());
        assertThat(response.image()).isEqualTo(createProductReqBody.image());
        assertThat(response.price()).isEqualTo(createProductReqBody.price());
        assertThat(response.pickupTime()).isEqualTo(createProductReqBody.pickupTime());
        assertThat(response.productType()).isEqualTo(createProductReqBody.productType());
        assertThat(response.productProviderName()).isEqualTo("McDonalds");

        ProductEntity p = productRepository.findByFoodId(response.foodId());
        assertThat(p).isNotNull();
        assertThat(p.getName()).isEqualTo(createProductReqBody.name());
        assertThat(p.getDescription()).isEqualTo(createProductReqBody.description());
        assertThat(p.getImage()).isEqualTo(createProductReqBody.image());
        assertThat(p.getPrice()).isEqualTo(createProductReqBody.price());
        assertThat(p.getPickupTime()).isEqualTo(createProductReqBody.pickupTime());
        assertThat(p.getProductType()).isEqualTo(createProductReqBody.productType());
        assertThat(p.getProductProviderId()).isEqualTo(createProductReqBody.productProviderId());

        FoodEntity f = foodRepository.findById(response.foodId()).orElseThrow();
        assertThat(f).isNotNull();
        assertThat(f.getVegetarian()).isEqualTo(createFoodReqBody.vegetarian());
        assertThat(f.getVegan()).isEqualTo(createFoodReqBody.vegan());
        assertThat(f.getGlutenFree()).isEqualTo(createFoodReqBody.glutenFree());
        assertThat(f.getNutFree()).isEqualTo(createFoodReqBody.nutFree());
        assertThat(f.getDairyFree()).isEqualTo(createFoodReqBody.dairyFree());
        assertThat(f.getOrganic()).isEqualTo(createFoodReqBody.organic());
        assertThat(f.getProductId()).isEqualTo(p.getId());

        // Cleanup
        foodRepository.deleteById(response.foodId());
        productRepository.deleteById(p.getId());
        assertThat(foodRepository.findById(response.foodId())).isEmpty();
        assertThat(productRepository.findByFoodId(response.foodId())).isNull();
    }

    @Test
    void testUpdateFood() throws Exception {
        // Given
        var createProductReqBody = new CreateProductReqBody(
                ProductType.FOOD,
                1L, // McDonalds
                "New Created Food",
                "New Created Food Description",
                "https://example.com/new-created-food.jpg",
                new BigDecimal("9.9900"),
                LocalDateTime.now().plusHours(1).withNano(0)
        );
        var createFoodReqBody = new CreateFoodReqBody(createProductReqBody, false, false, false, false, false, false);
        MvcResult createdResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/create-food")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFoodReqBody))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        var createdFoodResponse = objectMapper.readValue(createdResult.getResponse().getContentAsString(), GetFoodResponse.class);
        var foodId = createdFoodResponse.foodId();

        UpdateProductReqBody updateProductReqBody = new UpdateProductReqBody(
                "Updated - New Food",
                "Updated - New Created Food Description",
                "https://example.com/updated-new-food.jpg",
                new BigDecimal("10.9900"),
                LocalDateTime.now().plusHours(2).withNano(0)
        );
        UpdateFoodReqBody updateFoodReqBody = new UpdateFoodReqBody(false, false, false, false, false, false, updateProductReqBody);

        // When
        MvcResult updatedResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(controllerPath + "/update-food/%s".formatted(foodId))
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateFoodReqBody))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        var updatedFoodResponse = objectMapper.readValue(updatedResult.getResponse().getContentAsString(), GetFoodResponse.class);

        // Then
        assertThat(updatedFoodResponse).isNotNull();
        assertThat(updatedFoodResponse.name()).isEqualTo(updateProductReqBody.name());
        assertThat(updatedFoodResponse.description()).isEqualTo(updateProductReqBody.description());
        assertThat(updatedFoodResponse.image()).isEqualTo(updateProductReqBody.image());
        assertThat(updatedFoodResponse.price()).isEqualTo(updateProductReqBody.price());
        assertThat(updatedFoodResponse.pickupTime()).isEqualTo(updateProductReqBody.pickupTime());
        assertThat(updatedFoodResponse.productProviderName()).isEqualTo("McDonalds");

        ProductEntity p = productRepository.findByFoodId(updatedFoodResponse.foodId());
        assertThat(p).isNotNull();
        assertThat(p.getName()).isEqualTo(updateProductReqBody.name());
        assertThat(p.getDescription()).isEqualTo(updateProductReqBody.description());
        assertThat(p.getImage()).isEqualTo(updateProductReqBody.image());
        assertThat(p.getPrice()).isEqualTo(updateProductReqBody.price());
        assertThat(p.getPickupTime()).isEqualTo(updateProductReqBody.pickupTime());
        assertThat(p.getProductType()).isEqualTo(createdFoodResponse.productType());
        assertThat(p.getProductProviderId()).isEqualTo(createProductReqBody.productProviderId());

        FoodEntity f = foodRepository.findById(updatedFoodResponse.foodId()).orElseThrow();
        assertThat(f).isNotNull();
        assertThat(f.getVegetarian()).isEqualTo(createFoodReqBody.vegetarian());
        assertThat(f.getVegan()).isEqualTo(createFoodReqBody.vegan());
        assertThat(f.getGlutenFree()).isEqualTo(createFoodReqBody.glutenFree());
        assertThat(f.getNutFree()).isEqualTo(createFoodReqBody.nutFree());
        assertThat(f.getDairyFree()).isEqualTo(createFoodReqBody.dairyFree());
        assertThat(f.getOrganic()).isEqualTo(createFoodReqBody.organic());
        assertThat(f.getProductId()).isEqualTo(p.getId());

        // Cleanup
        foodRepository.deleteById(updatedFoodResponse.foodId());
        productRepository.deleteById(p.getId());
        assertThat(foodRepository.findById(updatedFoodResponse.foodId())).isEmpty();
        assertThat(productRepository.findByFoodId(updatedFoodResponse.foodId())).isNull();
    }

    @Test
    void testUpdateFoodTransactionContext() {
        // TODO implement
    }

    @Test
    void testDeleteFood() {
        // TODO implement
    }

    @Test
    void testUpdateFoodFoodCategories() {
        // TODO implement
    }

    @Test
    void testGetAllFoodCategories() {
        // TODO implement
    }

    @Test
    void testGetAllFoodCategoriesByIds() {
        // TODO implement
    }

    @Test
    void testGetAllFoodCategoriesForFood() {
        // TODO implement
    }

}
