package com.ffreaky.shoppingservice.product.service;

import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import com.ffreaky.shoppingservice.product.entity.ProductId;
import com.ffreaky.shoppingservice.product.model.request.CreateProductReqBody;
import com.ffreaky.shoppingservice.product.model.request.UpdateProductReqBody;
import com.ffreaky.shoppingservice.product.repository.ProductRepository;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity createProduct(CreateProductReqBody dto) {
        final ProductEntity pe = new ProductEntity();
        final ProductId productId = new ProductId();
        productId.setProductType(dto.productType());
        pe.setProductId(productId);
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
        pe.setName(dto.name());
        pe.setDescription(dto.description());
        pe.setPrice(dto.price());
        pe.setPickupTime(dto.pickupTime());
        return saveProduct(pe);
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
}
