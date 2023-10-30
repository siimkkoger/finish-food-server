package com.ffreaky.shoppingservice.food.service;

import com.ffreaky.shoppingservice.food.model.FoodCategoryDto;
import com.ffreaky.shoppingservice.product.ProductOrderBy;
import com.ffreaky.shoppingservice.food.entity.FoodEntity;
import com.ffreaky.shoppingservice.food.entity.FoodFoodCategoryEntity;
import com.ffreaky.shoppingservice.food.entity.QFoodEntity;
import com.ffreaky.shoppingservice.food.entity.QFoodFoodCategoryEntity;
import com.ffreaky.shoppingservice.food.model.request.CreateFoodReqBody;
import com.ffreaky.shoppingservice.food.model.request.GetFoodsFilter;
import com.ffreaky.shoppingservice.food.model.request.UpdateFoodFoodCategoriesReqBody;
import com.ffreaky.shoppingservice.food.model.request.UpdateFoodReqBody;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import com.ffreaky.shoppingservice.food.repository.FoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodFoodCategoryRepository;
import com.ffreaky.shoppingservice.food.repository.FoodRepository;
import com.ffreaky.shoppingservice.product.ProductType;
import com.ffreaky.shoppingservice.product.entity.ProductEntity;
import com.ffreaky.shoppingservice.product.entity.QProductEntity;
import com.ffreaky.shoppingservice.product.entity.QProductProviderEntity;
import com.ffreaky.shoppingservice.product.service.ProductService;
import com.ffreaky.utilities.SimpleIf;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    private final JPAQueryFactory queryFactory;

    private final DSLContext dsl;

    private final FoodRepository foodRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final FoodFoodCategoryRepository foodFoodCategoryRepository;
    private final ProductService productService;

    public FoodService(
            JPAQueryFactory queryFactory,
            DSLContext dsl,
            FoodRepository foodRepository,
            FoodCategoryRepository foodCategoryRepository,
            FoodFoodCategoryRepository foodFoodCategoryRepository,
            ProductService productService) {
        this.queryFactory = queryFactory;
        this.dsl = dsl;
        this.foodRepository = foodRepository;
        this.foodCategoryRepository = foodCategoryRepository;
        this.foodFoodCategoryRepository = foodFoodCategoryRepository;
        this.productService = productService;
    }

    public GetFoodResponse getFoodById(Long id) {
        // TODO - write test to check if not deleted
        return foodRepository.findDtoById(id)
                .orElseThrow(() -> new FinishFoodException(FinishFoodException.Type.ENTITY_NOT_FOUND, "Food with foodId " + id + " not found"));
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
        fe.setProductId(savedProductEntity.getId());
        fe.setProductType(ProductType.FOOD);
        fe.setVegetarian(reqBody.vegetarian());
        fe.setVegan(reqBody.vegan());
        fe.setGlutenFree(reqBody.glutenFree());
        fe.setNutFree(reqBody.nutFree());
        fe.setDairyFree(reqBody.dairyFree());
        fe.setOrganic(reqBody.organic());
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

    // TODO - implement cache based on how often a filter is used
    public List<GetFoodResponse> getFoods(GetFoodsFilter filter) {
        var f = QFoodEntity.foodEntity;
        var p = QProductEntity.productEntity;
        var pp = QProductProviderEntity.productProviderEntity;

        BooleanExpression condition = Expressions.asBoolean(true).isTrue();
        condition = condition.and(p.deletedAt.isNull());

        if (SimpleIf.isNotEmptyOrNull(filter.productProviderIds())) {
            condition = condition.and(pp.id.in(filter.productProviderIds()));
        }

        if (SimpleIf.isNotEmptyOrNull(filter.foodCategoryIds())) {
            var ffc = QFoodFoodCategoryEntity.foodFoodCategoryEntity;
            var filterMatchAll = filter.foodCategoryIdsMatchAll() != null && filter.foodCategoryIdsMatchAll();
            if (filterMatchAll) {
                long countRequired = filter.foodCategoryIds().size();
                condition = condition.and(f.id.in(
                        queryFactory.select(ffc.id.foodId)
                                .from(ffc)
                                .where(ffc.id.foodCategoryId.in(filter.foodCategoryIds()))
                                .groupBy(ffc.id.foodId)
                                .having(ffc.id.foodCategoryId.countDistinct().eq(countRequired))
                                .fetch()
                ));
            } else {
                condition = condition.and(f.id.in(queryFactory.select(ffc.id.foodId).from(ffc).where(ffc.id.foodCategoryId.in(filter.foodCategoryIds()))));
            }
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
                        f.vegetarian,
                        f.vegan,
                        f.glutenFree,
                        f.nutFree,
                        f.dairyFree,
                        f.organic,
                        p.price,
                        p.pickupTime,
                        p.productType,
                        pp.name))
                .from(f)
                .join(p).on(f.productId.eq(p.id))
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

        if (!dtoFieldsEqualEntity(req, fe)) {
            updateFoodEntityFromDto(fe, req);
        }

        return getFoodById(foodId);
    }

    private void updateFoodEntityFromDto(FoodEntity entity, UpdateFoodReqBody dto) {
        if (!Objects.equals(dto.vegetarian(), entity.getVegetarian())) entity.setVegetarian(dto.vegetarian());
        if (!Objects.equals(dto.vegan(), entity.getVegan())) entity.setVegan(dto.vegan());
        if (!Objects.equals(dto.glutenFree(), entity.getGlutenFree())) entity.setGlutenFree(dto.glutenFree());
        if (!Objects.equals(dto.nutFree(), entity.getNutFree())) entity.setNutFree(dto.nutFree());
        if (!Objects.equals(dto.dairyFree(), entity.getDairyFree())) entity.setDairyFree(dto.dairyFree());
        if (!Objects.equals(dto.organic(), entity.getOrganic())) entity.setOrganic(dto.organic());
    }

    private boolean dtoFieldsEqualEntity(UpdateFoodReqBody dto, FoodEntity entity) {
        return Objects.equals(dto.vegetarian(), entity.getVegetarian()) &&
                Objects.equals(dto.vegan(), entity.getVegan()) &&
                Objects.equals(dto.glutenFree(), entity.getGlutenFree()) &&
                Objects.equals(dto.nutFree(), entity.getNutFree()) &&
                Objects.equals(dto.dairyFree(), entity.getDairyFree()) &&
                Objects.equals(dto.organic(), entity.getOrganic());
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

    public Set<FoodCategoryDto> getAllFoodCategories() {
        return foodCategoryRepository.findAllCategories();
    }

    public Set<FoodCategoryDto> getAllFoodCategoriesForFood(Long foodId) {
        return foodCategoryRepository.findCategoriesByFoodId(foodId);
    }

    public boolean foodExists(Long foodId) {
        return foodRepository.existsById(foodId);
    }

}
