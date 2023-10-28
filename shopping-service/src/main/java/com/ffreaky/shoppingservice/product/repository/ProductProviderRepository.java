package com.ffreaky.shoppingservice.product.repository;

import com.ffreaky.shoppingservice.product.entity.ProductProviderEntity;
import com.ffreaky.shoppingservice.product.model.request.ProviderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductProviderRepository extends JpaRepository<ProductProviderEntity, Long> {

    @Query("SELECT new com.ffreaky.shoppingservice.product.model.request.ProviderResponse(p.id, p.name) FROM ProductProviderEntity p WHERE p.deletedAt IS NULL")
    public List<ProviderResponse> findAllNotDeleted();
}
