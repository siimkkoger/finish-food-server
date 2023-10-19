package com.ffreaky.shoppingservice.product.service;

import com.ffreaky.shoppingservice.product.ProductType;
import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import com.ffreaky.shoppingservice.product.entity.ProductId;
import com.ffreaky.shoppingservice.product.model.CreateProductRequestDto;
import com.ffreaky.shoppingservice.product.model.UpdateProductRequestDto;
import com.ffreaky.shoppingservice.product.repository.ProductRepository;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity createProduct(CreateProductRequestDto dto) {
        final ProductEntity pe = new ProductEntity();
        final ProductId productId = new ProductId();
        productId.setProductType(ProductType.FOOD);
        pe.setProductId(productId);
        pe.setProductProviderId(dto.productProviderId());
        pe.setName(dto.name());
        pe.setDescription(dto.description());
        pe.setImage(dto.image());
        pe.setPrice(dto.price());
        pe.setPickupTime(dto.pickupTime());

        return saveProduct(pe);
    }

    public ProductEntity updateProduct(Long productId, UpdateProductRequestDto dto) {
        ProductEntity pe = productRepository.findById(productId)
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Product not found"));
        pe.setName(dto.name());
        pe.setDescription(dto.description());
        pe.setPrice(dto.price());
        pe.setPickupTime(dto.pickupTime());
        return saveProduct(pe);
    }

    public ProductEntity saveProduct(ProductEntity pe) {
        final ProductEntity savedProductEntity;
        try {
            savedProductEntity = productRepository.save(pe);
        } catch (Exception e) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Error saving product: " + e.getMessage());
        }
        return savedProductEntity;
    }
}
