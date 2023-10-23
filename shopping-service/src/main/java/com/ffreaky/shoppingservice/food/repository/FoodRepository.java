package com.ffreaky.shoppingservice.food.repository;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.FoodDto;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Repository for food.
 *
 * Keeping some of the unused methods for future reference.
 * Replaced findAllDto and findAllByFoodCategoryIds with jOOQ conditional query.
 */
@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, Long> {

    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.FoodDto(
                f.id,
                p.name,
                p.description,
                p.image,
                f.dietaryRestrictions,
                p.price,
                p.pickupTime,
                p.productId.productType,
                pp.name
                )
            FROM FoodEntity f
            JOIN ProductEntity p
               ON f.productId = p.productId.id
               AND f.productType = p.productId.productType
            JOIN ProductProviderEntity pp
               ON p.productProviderId = pp.id
            WHERE f.id = :id
            """)
    Optional<GetFoodResponse> findDtoById(long id);

    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.FoodDto(
                f.id,
                p.name,
                p.description,
                p.image,
                f.dietaryRestrictions,
                p.price,
                p.pickupTime,
                p.productId.productType,
                pp.name
                )
            FROM FoodEntity f
            JOIN ProductEntity p
               ON f.productId = p.productId.id
               AND f.productType = p.productId.productType
            JOIN ProductProviderEntity pp
               ON p.productProviderId = pp.id
            """)
    Set<FoodDto> findAllDto();

    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.FoodDto(
                f.id,
                p.name,
                p.description,
                p.image,
                f.dietaryRestrictions,
                p.price,
                p.pickupTime,
                p.productId.productType,
                pp.name
                )
            FROM FoodEntity f
            JOIN ProductEntity p
               ON f.productId = p.productId.id
               AND f.productType = p.productId.productType
            JOIN ProductProviderEntity pp
               ON p.productProviderId = pp.id
            WHERE f.id in (
                   SELECT ffc.id.foodId
                   FROM FoodFoodCategoryEntity ffc
                   WHERE ffc.id.foodCategoryId in :foodCategoryIds
            )
            """)
    Set<FoodDto> findAllByFoodCategoryIds(Set<Long> foodCategoryIds);

}
