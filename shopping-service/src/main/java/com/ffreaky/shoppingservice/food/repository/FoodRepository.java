package com.ffreaky.shoppingservice.food.repository;

import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.model.FoodDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, Long> {

    @Query("""
             SELECT new com.ffreaky.shoppingservice.food.model.FoodDto(
                 f.id,
                 f.name,
                 f.description,
                 f.image,
                 f.dietaryRestrictions,
                 p.price,
                 p.pickupTime,
                 p.productId.productType,
                 pp.name
                 )
             FROM FoodEntity f
             JOIN ProductEntity p
                ON f.productId = p.productId.id
                AND f.productTypeName = p.productId.productType
             JOIN ProductProviderEntity pp
                ON p.productProviderId = pp.id
             WHERE f.id = :id
             """)
    Optional<FoodDto> findDtoById(long id);

    @Query("""
             SELECT new com.ffreaky.shoppingservice.food.model.FoodDto(
                 f.id,
                 f.name,
                 f.description,
                 f.image,
                 f.dietaryRestrictions,
                 p.price,
                 p.pickupTime,
                 p.productId.productType,
                 pp.name
                 )
             FROM FoodEntity f
             JOIN ProductEntity p
                ON f.productId = p.productId.id
                AND f.productTypeName = p.productId.productType
             JOIN ProductProviderEntity pp
                ON p.productProviderId = pp.id
             """)
    Set<FoodDto> findAllDto();

}
