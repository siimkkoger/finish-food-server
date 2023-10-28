package com.ffreaky.shoppingservice.product.controller;

import com.ffreaky.shoppingservice.product.model.request.ProviderResponse;
import com.ffreaky.shoppingservice.product.service.ProductService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/get-providers")
    public List<ProviderResponse> getProviders() {
        return productService.getProviders();
    }
}
