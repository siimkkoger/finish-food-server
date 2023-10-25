package com.ffreaky.shoppingservice.food;

import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import com.ffreaky.shoppingservice.food.repository.FoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodFoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodRepository;
import com.ffreaky.shoppingservice.food.service.FoodService;
import com.ffreaky.shoppingservice.product.ProductType;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "/application-test.properties")
public class FoodServiceTest {

    @Value("${spring.application.name}")
    String applicationName;

    @Autowired
    private FoodService foodService;

    @MockBean
    private FoodRepository foodRepository;

    @MockBean
    private FoodCategoryRepository foodCategoryRepository;

    @MockBean
    private FoodFoodCategoryRepository foodFoodCategoryRepository;

    @Test
    void test() {
        assertThat(applicationName).isEqualTo("shopping-service-test");
    }

    @Test
    void testGetFoodById() {
        // given
        long foodId = 1L;
        final GetFoodResponse foodResponse = exampleGetFoodResponse(foodId);
        given(foodRepository.findDtoById(foodId)).willReturn(Optional.of(foodResponse));

        // when
        GetFoodResponse food = foodService.getFoodById(foodId);

        // then
        assertThat(food).isEqualTo(foodResponse);

    }

    @Test
    void testGetFoodById_foodDoesntExist() {
        // given
        long foodId = 1L;
        given(foodRepository.findDtoById(foodId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> foodService.getFoodById(foodId))
                .isInstanceOf(FinishFoodException.class)
                .hasMessageContaining("Food with id " + foodId + " not found");
    }

    private GetFoodResponse exampleGetFoodResponse(Long id) {
        return new GetFoodResponse(
                1L,
                "name",
                "description",
                "image",
                "dietaryRestrictions",
                BigDecimal.valueOf(1.0),
                LocalDateTime.now(),
                ProductType.FOOD,
                "productProviderName"
        );
    }

}
