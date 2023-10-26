package com.ffreaky.shoppingservice.product;

import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import com.ffreaky.shoppingservice.product.model.request.CreateProductReqBody;
import com.ffreaky.shoppingservice.product.model.request.UpdateProductReqBody;
import com.ffreaky.shoppingservice.product.repository.ProductRepository;
import com.ffreaky.shoppingservice.product.service.ProductService;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

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
public class ProductServiceTest {

    @Value("${spring.application.name}")
    String applicationName;

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void test() {
        assertThat(applicationName).isEqualTo("shopping-service-test");
        assertThat(productService).isNotNull();
    }

    @Test
    void testCreateProduct() {
        // given
        long createdId = 1L;
        final CreateProductReqBody reqBody = exampleCreateProductReqBody();
        given(productRepository.save(productEntityArgumentMatcher(convertToEntity(reqBody, null))))
                .willReturn(convertToEntity(reqBody, createdId));

        // when
        final ProductEntity response = productService.createProduct(reqBody);

        // then
        assertThat(response.getId()).isEqualTo(createdId);
        assertThat(response.getProductType()).isEqualTo(reqBody.productType());
        assertThat(response.getProductProviderId()).isEqualTo(reqBody.productProviderId());
        assertThat(response.getName()).isEqualTo(reqBody.name());
        assertThat(response.getDescription()).isEqualTo(reqBody.description());
        assertThat(response.getImage()).isEqualTo(reqBody.image());
        assertThat(response.getPrice()).isEqualTo(reqBody.price());
        assertThat(response.getPickupTime()).isEqualTo(reqBody.pickupTime());
    }

    @Test
    void testCreateProduct_savingToDatabaseFailed() {
        // given
        final CreateProductReqBody reqBody = exampleCreateProductReqBody();
        final ProductEntity p = convertToEntity(reqBody, null);
        given(productRepository.save(productEntityArgumentMatcher(p)))
                .willThrow(new RuntimeException("Error saving product"));

        // then
        assertThatThrownBy(() -> productService.createProduct(reqBody))
                .isInstanceOf(FinishFoodException.class)
                .hasMessageContaining("Error saving product");
    }

    private CreateProductReqBody exampleCreateProductReqBody() {
        return new CreateProductReqBody(
                ProductType.FOOD,
                1L,
                "name",
                "description",
                "image",
                new BigDecimal("1.00"),
                LocalDateTime.now()
        );
    }

    private ProductEntity convertToEntity(CreateProductReqBody req, Long id) {
        final ProductEntity pe = new ProductEntity();
        pe.setProductType(req.productType());
        if (id != null) {
            pe.setId(id);
        }
        pe.setProductProviderId(req.productProviderId());
        pe.setName(req.name());
        pe.setDescription(req.description());
        pe.setImage(req.image());
        pe.setPrice(req.price());
        pe.setPickupTime(req.pickupTime());
        return pe;
    }

    private ProductEntity productEntityArgumentMatcher(ProductEntity p) {
        return argThat(
                pe -> pe.getProductType().equals(p.getProductType()) &&
                        pe.getProductProviderId().equals(p.getProductProviderId()) &&
                        pe.getName().equals(p.getName()) &&
                        pe.getDescription().equals(p.getDescription()) &&
                        pe.getImage().equals(p.getImage()) &&
                        pe.getPrice().equals(p.getPrice()) &&
                        pe.getPickupTime().equals(p.getPickupTime()));
    }

    @Test
    void testUpdateProduct() {
        // given
        long updatedId = 1L;
        long productProviderId = 1L;
        ProductType productType = ProductType.FOOD;
        final UpdateProductReqBody reqBody = exampleUpdateProductReqBody();

        final ProductEntity entityBefore = convertToEntity(reqBody, updatedId, productType, productProviderId);
        entityBefore.setName("nameBefore");
        entityBefore.setDescription("descriptionBefore");
        entityBefore.setPrice(new BigDecimal("2.00"));
        entityBefore.setPickupTime(LocalDateTime.now().minusDays(1));

        final ProductEntity entityAfter = convertToEntity(reqBody, updatedId, productType, productProviderId);
        entityAfter.setName(reqBody.name());
        entityAfter.setDescription(reqBody.description());
        entityAfter.setPrice(reqBody.price());
        entityAfter.setPickupTime(reqBody.pickupTime());

        given(productRepository.findById(updatedId)).willReturn(Optional.of(entityBefore)).getMock();
        given(productRepository.save(productEntityArgumentMatcher(entityAfter))).willReturn(entityAfter);

        // when
        final ProductEntity response = productService.updateProduct(updatedId, reqBody);

        // then
        assertThat(response.getId()).isEqualTo(updatedId);
        assertThat(response.getProductType()).isEqualTo(productType);
        assertThat(response.getProductProviderId()).isEqualTo(productProviderId);
        assertThat(response.getName()).isEqualTo(reqBody.name());
        assertThat(response.getDescription()).isEqualTo(reqBody.description());
        assertThat(response.getImage()).isEqualTo(reqBody.image());
        assertThat(response.getPrice()).isEqualTo(reqBody.price());
        assertThat(response.getPickupTime()).isEqualTo(reqBody.pickupTime());
    }

    @Test
    void testUpdateProduct_invalidId() {
        // given
        long updatedId = 1L;
        given(productRepository.findById(updatedId)).willReturn(Optional.empty()).getMock();

        // then
        assertThatThrownBy(() -> productService.updateProduct(updatedId, exampleUpdateProductReqBody()))
                .isInstanceOf(FinishFoodException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void testUpdateProduct_savingToDatabaseFailed() {
        // given
        long updatedId = 1L;
        long productProviderId = 1L;
        ProductType productType = ProductType.FOOD;
        final UpdateProductReqBody reqBody = exampleUpdateProductReqBody();

        final ProductEntity entityBefore = convertToEntity(reqBody, updatedId, productType, productProviderId);
        entityBefore.setName("nameBefore");
        entityBefore.setDescription("descriptionBefore");
        entityBefore.setPrice(new BigDecimal("2.00"));
        entityBefore.setPickupTime(LocalDateTime.now().minusDays(1));

        final ProductEntity entityAfter = convertToEntity(reqBody, updatedId, productType, productProviderId);
        entityAfter.setName(reqBody.name());
        entityAfter.setDescription(reqBody.description());
        entityAfter.setPrice(reqBody.price());
        entityAfter.setPickupTime(reqBody.pickupTime());

        given(productRepository.findById(updatedId)).willReturn(Optional.of(entityBefore)).getMock();
        given(productRepository.save(productEntityArgumentMatcher(entityAfter)))
                .willThrow(new RuntimeException("Error saving product"));

        // then
        assertThatThrownBy(() -> productService.updateProduct(updatedId, reqBody))
                .isInstanceOf(FinishFoodException.class)
                .hasMessageContaining("Error saving product");
    }

    private UpdateProductReqBody exampleUpdateProductReqBody() {
        return new UpdateProductReqBody(
                "name",
                "description",
                "image",
                new BigDecimal("1.00"),
                LocalDateTime.now()
        );
    }

    private ProductEntity convertToEntity(UpdateProductReqBody req, Long id, ProductType productType, long productProviderId) {
        final ProductEntity pe = new ProductEntity();
        pe.setProductType(productType);
        pe.setId(id);
        pe.setProductProviderId(productProviderId);
        pe.setName(req.name());
        pe.setDescription(req.description());
        pe.setImage(req.image());
        pe.setPrice(req.price());
        pe.setPickupTime(req.pickupTime());
        return pe;
    }
}
