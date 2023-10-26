package com.ffreaky.shoppingservice.food.service;

import com.ffreaky.shoppingservice.product.ProductOrderBy;
import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.entity.FoodFoodCategoryEntity;
import com.ffreaky.shoppingservice.food.entity.QFoodEntity;
import com.ffreaky.shoppingservice.food.entity.QFoodFoodCategoryEntity;
import com.ffreaky.shoppingservice.food.model.request.CreateFoodReqBody;
import com.ffreaky.shoppingservice.food.model.request.GetFoodsFilter;
import com.ffreaky.shoppingservice.food.model.request.UpdateFoodFoodCategoriesReqBody;
import com.ffreaky.shoppingservice.food.model.request.UpdateFoodReqBody;
import com.ffreaky.shoppingservice.food.model.response.GetFoodCategoryResponse;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import com.ffreaky.shoppingservice.food.repository.FoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodFoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodRepository;
import com.ffreaky.shoppingservice.product.ProductType;
import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import com.ffreaky.shoppingservice.product.entity.QProductEntity;
import com.ffreaky.shoppingservice.product.entity.QProductProviderEntity;
import com.ffreaky.shoppingservice.product.service.ProductService;
import com.ffreaky.utilities.exceptions.FinishFoodException;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Service
public class FoodService {

    static Logger logger = LoggerFactory.getLogger(FoodService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final DSLContext dsl;

    private final FoodRepository foodRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final FoodFoodCategoryRepository foodFoodCategoryRepository;
    private final ProductService productService;

    public FoodService(
            DSLContext dsl,
            FoodRepository foodRepository,
            FoodCategoryRepository foodCategoryRepository,
            FoodFoodCategoryRepository foodFoodCategoryRepository,
            ProductService productService) {
        this.dsl = dsl;
        this.foodRepository = foodRepository;
        this.foodCategoryRepository = foodCategoryRepository;
        this.foodFoodCategoryRepository = foodFoodCategoryRepository;
        this.productService = productService;
    }

    public GetFoodResponse getFoodById(Long id) {
        // TODO - write test to check if not deleted
        return foodRepository.findDtoById(id)
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food with id " + id + " not found"));
    }

    /**
     * Add a new food row to the database.
     * The method also adds everything else that is needed for the food to be complete:
     * 1. ProductEntity
     * 2. FoodEntity
     * 3. FoodFoodCategoryEntity
     *
     * @param reqBody
     * @return GetFoodResponse
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public GetFoodResponse createFood(CreateFoodReqBody reqBody) {
        // Check that product category is FOOD
        if (!reqBody.product().productType().equals(ProductType.FOOD)) {
            throw new FinishFoodException(FinishFoodException.Type.INVALID_PRODUCT_TYPE, "Product type must be FOOD");
        }

        // Create and save product
        final ProductEntity savedProductEntity = productService.createProduct(reqBody.product());

        // Create food
        final FoodEntity fe = new FoodEntity();
        fe.setProductId(savedProductEntity.getProductId().getId());
        fe.setProductType(ProductType.FOOD);
        fe.setDietaryRestrictions(reqBody.dietaryRestrictions());
        final FoodEntity savedFoodEntity = saveFoodEntity(fe);

        // Return GetFoodResponse
        return getFoodById(savedFoodEntity.getId());
    }

    private FoodEntity saveFoodEntity(FoodEntity fe) {
        final FoodEntity savedFoodEntity;
        try {
            savedFoodEntity = foodRepository.save(fe);
        } catch (Exception e) {
            throw new FinishFoodException(FinishFoodException.Type.SERVER_ERROR, "Error saving food: " + e.getMessage());
        }
        return savedFoodEntity;
    }

    @Deprecated // Use getFoods instead (uses querydsl)
    public List<GetFoodResponse> getFoodsJooq(GetFoodsFilter filter) {
        // Apply filters based on user input
        Condition condition = DSL.noCondition();
        if (filter.foodCategoryIds() != null && !filter.foodCategoryIds().isEmpty()) {
            condition = condition.and(field("FOOD.ID", Long.class).in(
                    dsl.select(field("FOOD_FOOD_CATEGORY.FOOD_ID", Long.class))
                            .from(table("FOOD_FOOD_CATEGORY"))
                            .where(field("FOOD_FOOD_CATEGORY.FOOD_CATEGORY_ID", Long.class).in(filter.foodCategoryIds()))
            ));
        }

        // Define the base query
        var query = dsl.select(
                        field("FOOD.ID", Long.class),
                        field("PRODUCT.NAME", String.class),
                        field("PRODUCT.DESCRIPTION", String.class),
                        field("PRODUCT.IMAGE", String.class),
                        field("FOOD.DIETARY_RESTRICTIONS", String.class),
                        field("PRODUCT.PRICE", Double.class),
                        field("PRODUCT.PICKUP_TIME", Date.class),
                        field("PRODUCT.PRODUCT_TYPE_NAME", String.class),
                        field("PRODUCT_PROVIDER.NAME", String.class))
                .from(table("FOOD"))
                .join(table("PRODUCT"))
                .on(field("FOOD.PRODUCT_ID", Long.class).eq(field("PRODUCT.ID", Long.class))
                        .and(field("FOOD.PRODUCT_TYPE_NAME", String.class).eq(field("PRODUCT.PRODUCT_TYPE_NAME", String.class))))
                .join(table("PRODUCT_PROVIDER"))
                .on(field("PRODUCT.PRODUCT_PROVIDER_ID", Long.class).eq(field("PRODUCT_PROVIDER.ID", Long.class)))
                .where(condition);

        // Execute the query
        var result = query.fetch();

        logger.info("Query: " + query.getSQL());

        // Map the result to a Set of FoodDto
        return result.into(GetFoodResponse.class);
    }


    public List<GetFoodResponse> getFoods(GetFoodsFilter filter) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QFoodEntity f = QFoodEntity.foodEntity;
        QProductEntity p = QProductEntity.productEntity;
        QProductProviderEntity pp = QProductProviderEntity.productProviderEntity;

        BooleanExpression condition = Expressions.asBoolean(true).isTrue();
        condition = condition.and(p.deletedAt.isNull());

        if (filter.foodCategoryIds() != null && !filter.foodCategoryIds().isEmpty()) {
            QFoodFoodCategoryEntity ffc = QFoodFoodCategoryEntity.foodFoodCategoryEntity;
            condition = condition.and(f.id.in(queryFactory.select(ffc.id.foodId).from(ffc).where(ffc.id.foodCategoryId.in(filter.foodCategoryIds()))));
        }
        if (filter.productProviderName() != null && !filter.productProviderName().isEmpty()) {
            condition = condition.and(pp.name.containsIgnoreCase(filter.productProviderName()));
        }
        if (filter.pickupTimeFrom() != null || filter.pickupTimeTo() != null) {
            condition = condition.and(p.pickupTime.between(filter.pickupTimeFrom(), filter.pickupTimeTo()));
        }

        var offset = (filter.page() - 1) * filter.pageSize();
        var orderSpecifier = orderSpecifier(filter.orderBy(), filter.direction());

        return queryFactory
                .select(Projections.constructor(GetFoodResponse.class,
                        f.id,
                        p.name,
                        p.description,
                        p.image,
                        f.dietaryRestrictions,
                        p.price,
                        p.pickupTime,
                        p.productId.productType,
                        pp.name))
                .from(f)
                .join(p).on(f.productId.eq(p.productId.id))
                .join(pp).on(p.productProviderId.eq(pp.id))
                .where(condition)
                .offset(offset)
                .limit(filter.pageSize())
                .orderBy(orderSpecifier)
                .fetch();
    }

    private OrderSpecifier<?> orderSpecifier(ProductOrderBy orderBy, Order direction) {
        return switch (orderBy) {
            case ID -> direction.equals(Order.ASC) ? QFoodEntity.foodEntity.id.asc() : QFoodEntity.foodEntity.id.desc();
            case NAME ->
                    direction.equals(Order.ASC) ? QProductEntity.productEntity.name.asc() : QProductEntity.productEntity.name.desc();
            case PRICE ->
                    direction.equals(Order.ASC) ? QProductEntity.productEntity.price.asc() : QProductEntity.productEntity.price.desc();
            case PICKUP_TIME ->
                    direction.equals(Order.ASC) ? QProductEntity.productEntity.pickupTime.asc() : QProductEntity.productEntity.pickupTime.desc();
            case CREATED_AT ->
                    direction.equals(Order.ASC) ? QProductEntity.productEntity.createdAt.asc() : QProductEntity.productEntity.createdAt.desc();
        };
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public GetFoodResponse updateFood(Long foodId, UpdateFoodReqBody req) {
        FoodEntity fe = foodRepository.findById(foodId)
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + foodId));

        // Save product entity
        productService.updateProduct(fe.getProductId(), req.product());

        // Update food entity
        if (req.dietaryRestrictions().equals(fe.getDietaryRestrictions())) {
            fe.setDietaryRestrictions(req.dietaryRestrictions());
            saveFoodEntity(fe);
        }

        return getFoodById(foodId);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void createOrUpdateFoodFoodCategories(Long foodId, UpdateFoodFoodCategoriesReqBody req) {
        try {
            // Delete all food categories
            foodFoodCategoryRepository.deleteAll(foodFoodCategoryRepository.findAllByFoodId(foodId));

            // Create and save food categories
            foodFoodCategoryRepository.saveAll(
                    req.foodCategoryIds().stream()
                            .map(id -> {
                                final FoodFoodCategoryEntity ffc = new FoodFoodCategoryEntity();
                                ffc.getId().setFoodId(foodId);
                                ffc.getId().setFoodCategoryId(id);
                                return ffc;
                            }).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new FinishFoodException(FinishFoodException.Type.BAD_REQUEST, "Error saving food categories: " + e.getMessage());
        }
    }

    public boolean deleteFood(Long id) {
        if (!foodRepository.existsById(id)) {
            throw new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food not found with ID: " + id);
        }

        foodRepository.deleteById(id);
        return true;
    }

    public GetFoodCategoryResponse getAllFoodCategories() {
        return new GetFoodCategoryResponse(foodCategoryRepository.findAllCategories());
    }

    public GetFoodCategoryResponse getAllFoodCategoriesForFood(Long foodId) {
        return new GetFoodCategoryResponse(foodCategoryRepository.findCategoriesByFoodId(foodId));
    }

}
