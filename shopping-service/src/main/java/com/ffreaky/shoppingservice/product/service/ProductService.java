package com.ffreaky.shoppingservice.product.service;

import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import com.ffreaky.shoppingservice.product.model.request.CreateProductReqBody;
import com.ffreaky.shoppingservice.product.model.request.ProviderResponse;
import com.ffreaky.shoppingservice.product.model.request.UpdateProductReqBody;
import com.ffreaky.shoppingservice.product.repository.ProductProviderRepository;
import com.ffreaky.shoppingservice.product.repository.ProductRepository;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductProviderRepository productProviderRepository;

    public ProductService(ProductRepository productRepository, ProductProviderRepository productProviderRepository) {
        this.productRepository = productRepository;
        this.productProviderRepository = productProviderRepository;
    }

    public ProductEntity createProduct(CreateProductReqBody dto) {
        final ProductEntity pe = new ProductEntity();
        pe.setProductType(dto.productType());
        pe.setProductProviderId(dto.productProviderId());
        pe.setName(dto.name());
        pe.setDescription(dto.description());
        pe.setImage(dto.image());
        pe.setPrice(dto.price());
        pe.setPickupTime(dto.pickupTime());

        return saveProduct(pe);
    }

    public ProductEntity updateProduct(Long productId, UpdateProductReqBody dto) {
        ProductEntity pe = productRepository.findById(productId)
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Product not found"));

        if (dtoFieldsEqualEntity(dto, pe)) {
            return pe;
        }

        updateProductEntityFromDto(dto, pe);
        return saveProduct(pe);
    }

    private void updateProductEntityFromDto(UpdateProductReqBody dto, ProductEntity entity) {
        if (!Objects.equals(dto.name(), entity.getName())) entity.setName(dto.name());
        if (!Objects.equals(dto.description(), entity.getDescription())) entity.setDescription(dto.description());
        if (!Objects.equals(dto.image(), entity.getImage())) entity.setImage(dto.image());
        if (!Objects.equals(dto.price(), entity.getPrice())) entity.setPrice(dto.price());
        if (!Objects.equals(dto.pickupTime(), entity.getPickupTime())) entity.setPickupTime(dto.pickupTime());
    }

    private boolean dtoFieldsEqualEntity(UpdateProductReqBody dto, ProductEntity entity) {
        return Objects.equals(dto.name(), entity.getName()) &&
                Objects.equals(dto.price(), entity.getPrice()) &&
                Objects.equals(dto.pickupTime(), entity.getPickupTime()) &&
                Objects.equals(dto.description(), entity.getDescription()) &&
                Objects.equals(dto.image(), entity.getImage());
    }

    private ProductEntity saveProduct(ProductEntity pe) {
        final ProductEntity savedProductEntity;
        try {
            savedProductEntity = productRepository.save(pe);
        } catch (Exception e) {
            throw new FinishFoodException(FinishFoodException.Type.SERVER_ERROR, "Error saving product: " + e.getMessage());
        }
        return savedProductEntity;
    }

    public List<ProviderResponse> getProviders() {
        return productProviderRepository.findAllNotDeleted();
    }
}
