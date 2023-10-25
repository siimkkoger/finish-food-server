package com.ffreaky.shoppingservice.food;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.request.CreateFoodReqBody;
import com.ffreaky.shoppingservice.food.model.request.UpdateFoodReqBody;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import com.ffreaky.shoppingservice.food.repository.FoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodFoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodRepository;
import com.ffreaky.shoppingservice.food.service.FoodService;
import com.ffreaky.shoppingservice.product.ProductType;
import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import com.ffreaky.shoppingservice.product.entity.ProductId;
import com.ffreaky.shoppingservice.product.model.request.CreateProductReqBody;
import com.ffreaky.shoppingservice.product.model.request.UpdateProductReqBody;
import com.ffreaky.shoppingservice.product.service.ProductService;
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
import static org.mockito.ArgumentMatchers.argThat;
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
    private ProductService productService;

    @MockBean
    private FoodRepository foodRepository;

    @MockBean
    private FoodCategoryRepository foodCategoryRepository;

    @MockBean
    private FoodFoodCategoryRepository foodFoodCategoryRepository;

    private static final String PRODUCT_PROVIDER_NAME = "productProviderName";

    @Test
    void test() {
        assertThat(applicationName).isEqualTo("shopping-service-test");
    }

    @Test
    void testGetFoodById() {
        // given
        final long FOOD_ID = 1L;
        final BigDecimal FOOD_PRICE = new BigDecimal("1.00");
        final String IMAGE = "image";
        final String DESCRIPTION = "description";
        final String FOOD_NAME = "name";
        final String DIETARY_RESTRICTIONS = "dietaryRestrictions";

        final GetFoodResponse foodResponse = new GetFoodResponse(
                FOOD_ID,
                FOOD_NAME,
                DESCRIPTION,
                IMAGE,
                DIETARY_RESTRICTIONS,
                FOOD_PRICE,
                LocalDateTime.now(),
                ProductType.FOOD,
                PRODUCT_PROVIDER_NAME
        );
        given(foodRepository.findDtoById(FOOD_ID)).willReturn(Optional.of(foodResponse));

        // when
        GetFoodResponse food = foodService.getFoodById(FOOD_ID);

        // then
        assertThat(food).isEqualTo(foodResponse);
    }

    @Test
    void testGetFoodById_foodDoesntExist() {
        // given
        final long FOOD_ID = 1L;
        given(foodRepository.findDtoById(FOOD_ID)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> foodService.getFoodById(FOOD_ID))
                .isInstanceOf(FinishFoodException.class)
                .hasMessageContaining("Food with id " + FOOD_ID + " not found");
    }

    @Test
    void testGetFoods() {
        // TODO - should be tested with e2e - mocking would leave out too many cases
    }

    @Test
    void testCreateFood() {
        // given
        final long PRODUCT_PROVIDER_ID = 1L;
        final BigDecimal FOOD_PRICE = new BigDecimal("1.00");
        final String IMAGE = "image";
        final String DESCRIPTION = "description";
        final String FOOD_NAME = "name";
        final String DIETARY_RESTRICTIONS = "dietaryRestrictions";

        final CreateProductReqBody productReqBody = new CreateProductReqBody(
                ProductType.FOOD,
                PRODUCT_PROVIDER_ID,
                FOOD_NAME,
                DESCRIPTION,
                IMAGE,
                FOOD_PRICE,
                LocalDateTime.now()
        );
        final CreateFoodReqBody reqBody = new CreateFoodReqBody(productReqBody, DIETARY_RESTRICTIONS);

        final var productId = 1L;
        final var p = new ProductEntity();
        p.setProductId(new ProductId(productId, ProductType.FOOD));
        p.setProductProviderId(PRODUCT_PROVIDER_ID);
        p.setPrice(FOOD_PRICE);
        p.setName(FOOD_NAME);
        p.setDescription(DESCRIPTION);
        p.setImage(IMAGE);
        p.setPickupTime(LocalDateTime.now());

        // ProductService tested in ProductServiceTest
        given(productService.createProduct(productReqBody)).willReturn(p);

        final FoodEntity foodEntity = toFoodEntity(reqBody, productId);
        final FoodEntity savedFoodEntity = toFoodEntity(reqBody, productId);
        savedFoodEntity.setId(1L);
        given(foodRepository.save(foodEntityArgumentMatcher(foodEntity))).willReturn(savedFoodEntity);

        final GetFoodResponse getFoodResponse = new GetFoodResponse(
                savedFoodEntity.getId(),
                reqBody.product().name(),
                reqBody.product().description(),
                reqBody.product().image(),
                reqBody.dietaryRestrictions(),
                reqBody.product().price(),
                reqBody.product().pickupTime(),
                ProductType.FOOD,
                PRODUCT_PROVIDER_NAME
        );
        given(foodRepository.findDtoById(savedFoodEntity.getId())).willReturn(Optional.of(getFoodResponse));

        // when
        final GetFoodResponse response = foodService.createFood(reqBody);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.productType()).isEqualTo(ProductType.FOOD);
        assertThat(response.productProviderName()).isEqualTo(PRODUCT_PROVIDER_NAME);
        assertThat(response.name()).isEqualTo(reqBody.product().name());
        assertThat(response.description()).isEqualTo(reqBody.product().description());
        assertThat(response.image()).isEqualTo(reqBody.product().image());
        assertThat(response.dietaryRestrictions()).isEqualTo(reqBody.dietaryRestrictions());
        assertThat(response.price()).isEqualTo(reqBody.product().price());
        assertThat(response.pickupTime()).isEqualTo(reqBody.product().pickupTime());
    }

    @Test
    void testCreateFood_savingToDatabaseFailed() {
        // given
        final long PRODUCT_PROVIDER_ID = 1L;
        final BigDecimal FOOD_PRICE = new BigDecimal("1.00");
        final String IMAGE = "image";
        final String DESCRIPTION = "description";
        final String FOOD_NAME = "name";
        final String DIETARY_RESTRICTIONS = "dietaryRestrictions";

        final CreateProductReqBody productReqBody = new CreateProductReqBody(
                ProductType.FOOD,
                PRODUCT_PROVIDER_ID,
                FOOD_NAME,
                DESCRIPTION,
                IMAGE,
                FOOD_PRICE,
                LocalDateTime.now()
        );
        final CreateFoodReqBody reqBody = new CreateFoodReqBody(productReqBody, DIETARY_RESTRICTIONS);

        final var productId = 1L;
        final var p = new ProductEntity();
        p.setProductId(new ProductId(productId, ProductType.FOOD));
        p.setProductProviderId(PRODUCT_PROVIDER_ID);
        p.setPrice(FOOD_PRICE);
        p.setName(FOOD_NAME);
        p.setDescription(DESCRIPTION);
        p.setImage(IMAGE);
        p.setPickupTime(LocalDateTime.now());

        // ProductService tested in ProductServiceTest
        given(productService.createProduct(productReqBody)).willReturn(p);

        final FoodEntity f = toFoodEntity(reqBody, productId);
        given(foodRepository.save(foodEntityArgumentMatcher(f)))
                .willThrow(new RuntimeException("Error saving food"));

        // then
        assertThatThrownBy(() -> foodService.createFood(reqBody))
                .isInstanceOf(FinishFoodException.class)
                .hasMessageContaining("Error saving food");
    }

    private FoodEntity foodEntityArgumentMatcher(FoodEntity f) {
        return argThat(
                fe -> fe.getProductId().equals(f.getProductId())
                        && fe.getProductType().equals(f.getProductType())
                        && fe.getDietaryRestrictions().equals(f.getDietaryRestrictions()));
    }

    private FoodEntity toFoodEntity(CreateFoodReqBody reqBody, Long productId) {
        final FoodEntity fe = new FoodEntity();
        fe.setProductId(productId);
        fe.setProductType(ProductType.FOOD);
        fe.setDietaryRestrictions(reqBody.dietaryRestrictions());
        return fe;
    }

    private FoodEntity toFoodEntity(UpdateFoodReqBody reqBody, Long foodId, Long productId) {
        final FoodEntity fe = new FoodEntity();
        fe.setId(foodId);
        fe.setProductId(productId);
        fe.setProductType(ProductType.FOOD);
        fe.setDietaryRestrictions(reqBody.dietaryRestrictions());
        return fe;
    }

    @Test
    void testUpdateFood() {
        // given
        final long PRODUCT_ID = 1L;
        final long FOOD_ID = 1L;
        final long PRODUCT_PROVIDER_ID = 1L;
        final BigDecimal FOOD_PRICE = new BigDecimal("1.00");
        final String IMAGE = "image";
        final String DESCRIPTION = "description";
        final String FOOD_NAME = "name";
        final String DIETARY_RESTRICTIONS = "dietaryRestrictions";

        final var productReqBody = new UpdateProductReqBody(FOOD_NAME, DESCRIPTION, IMAGE, FOOD_PRICE, LocalDateTime.now());
        final var foodReqBody = new UpdateFoodReqBody(DIETARY_RESTRICTIONS, productReqBody);

        var p = new ProductEntity();
        p.setProductId(new ProductId(PRODUCT_ID, ProductType.FOOD));
        p.setProductProviderId(PRODUCT_PROVIDER_ID);
        p.setPrice(FOOD_PRICE);
        p.setName(FOOD_NAME);
        p.setDescription(DESCRIPTION);
        p.setImage(IMAGE);
        p.setPickupTime(LocalDateTime.now());

        given(productService.updateProduct(PRODUCT_ID, productReqBody)).willReturn(p);

        final var reqBody = new UpdateFoodReqBody(DIETARY_RESTRICTIONS, productReqBody);

        final var oldFoodEntity = toFoodEntity(reqBody, FOOD_ID, PRODUCT_ID);
        oldFoodEntity.setDietaryRestrictions(DIETARY_RESTRICTIONS + "_OLD");
        final var savedFoodEntity = toFoodEntity(reqBody, FOOD_ID, PRODUCT_ID);

        given(foodRepository.findById(FOOD_ID)).willReturn(Optional.of(oldFoodEntity));
        given(foodRepository.save(foodEntityArgumentMatcher(savedFoodEntity))).willReturn(savedFoodEntity);

        final GetFoodResponse getFoodResponse = new GetFoodResponse(
                FOOD_ID,
                reqBody.product().name(),
                reqBody.product().description(),
                reqBody.product().image(),
                reqBody.dietaryRestrictions(),
                reqBody.product().price(),
                reqBody.product().pickupTime(),
                ProductType.FOOD,
                PRODUCT_PROVIDER_NAME
        );
        given(foodRepository.findDtoById(savedFoodEntity.getId())).willReturn(Optional.of(getFoodResponse));

        // when
        final GetFoodResponse response = foodService.updateFood(FOOD_ID, foodReqBody);

        // then
        assertThat(response.id()).isEqualTo(FOOD_ID);
        assertThat(response.productType()).isEqualTo(ProductType.FOOD);
        assertThat(response.productProviderName()).isEqualTo(PRODUCT_PROVIDER_NAME);
        assertThat(response.name()).isEqualTo(reqBody.product().name());
        assertThat(response.description()).isEqualTo(reqBody.product().description());
        assertThat(response.image()).isEqualTo(reqBody.product().image());
        assertThat(response.dietaryRestrictions()).isEqualTo(reqBody.dietaryRestrictions());
        assertThat(response.price()).isEqualTo(reqBody.product().price());
        assertThat(response.pickupTime()).isEqualTo(reqBody.product().pickupTime());
    }

    @Test
    void testCreateOrUpdateFoodFoodCategories() {
        // TODO
    }

    @Test
    void testGetAllFoodCategories() {
        // TODO
    }

    @Test
    void testGetAllFoodCategories_byIds() {
        // TODO
    }

}
