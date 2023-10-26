package com.ffreaky.shoppingservice.food.repository;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, Long> {

    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.response.GetFoodResponse(
                f.id,
                p.name,
                p.description,
                p.image,
                f.dietaryRestrictions,
                p.price,
                p.pickupTime,
                p.productType,
                pp.name
                )
            FROM FoodEntity f
            JOIN ProductEntity p
               ON f.productId = p.id
               AND f.productType = p.productType
            JOIN ProductProviderEntity pp
               ON p.productProviderId = pp.id
            WHERE f.id = :id
                AND p.deletedAt IS NULL
            """)
    Optional<GetFoodResponse> findDtoById(long id);

}
