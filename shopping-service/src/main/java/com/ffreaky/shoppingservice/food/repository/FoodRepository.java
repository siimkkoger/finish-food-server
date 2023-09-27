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
                f.product.price,
                f.product.pickupTime,
                f.product.productType.productType,
                f.product.productProvider.name)
            FROM FoodEntity f WHERE f.id = :id
            """)
    Optional<FoodDto> findDtoById(long id);


    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.FoodDto(
                            f.id,
                            f.name,
                            f.description,
                            f.image,
                            f.dietaryRestrictions,
                            f.product.price,
                            f.product.pickupTime,
                            f.product.productType.productType,
                            f.product.productProvider.name)
                        FROM FoodEntity f WHERE f.id in :ids
            """)
    Set<FoodDto> findAllDtoByIds(Set<Long> ids);

    @Query("""
            SELECT new com.ffreaky.shoppingservice.food.model.FoodDto(
                            f.id,
                            f.name,
                            f.description,
                            f.image,
                            f.dietaryRestrictions,
                            f.product.price,
                            f.product.pickupTime,
                            f.product.productType.productType,
                            f.product.productProvider.name)
                        FROM FoodEntity f
            """)
    Set<FoodDto> findAllDto();

}
