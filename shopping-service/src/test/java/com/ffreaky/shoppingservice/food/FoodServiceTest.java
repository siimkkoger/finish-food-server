package com.ffreaky.shoppingservice.food;

import com.ffreaky.shoppingservice.food.repository.FoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodFoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodRepository;
import com.ffreaky.shoppingservice.food.service.FoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

}
