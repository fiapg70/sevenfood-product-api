package br.com.sevenfood.product.sevenfoodproductapi.factory;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;

import java.math.BigDecimal;
import java.util.UUID;

public class ObjectFactory {
    public static ObjectFactory instance;

    private ObjectFactory() {}

    public static ObjectFactory getInstance() {
        if (instance == null) {
            instance = new ObjectFactory();
        }
        return instance;
    }

    public RestaurantEntity getRestaurantEntity() {
        return RestaurantEntity.builder()
                .name("Seven Food")
                .cnpj("02.365.347/0001-63")
                .build();
    }

    public Restaurant getRestaurant() {
        return Restaurant.builder()
                .name("Seven Food")
                .cnpj("02.365.347/0001-63")
                .build();
    }

    public ProductEntity getProductEntity(RestaurantEntity restaurantEntity, ProductCategoryEntity productCategory) {
        return ProductEntity.builder()
                .name("Coca-Cola")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategory(productCategory)
                .restaurant(restaurantEntity)
                .build();
    }

    public ProductEntity getProductEntity1(RestaurantEntity restaurantEntity, ProductCategoryEntity productCategory) {
        return ProductEntity.builder()
                .name("Bebida 1")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategory(productCategory)
                .restaurant(restaurantEntity)
                .build();
    }

    public ProductEntity getProductEntity2(RestaurantEntity restaurantEntity, ProductCategoryEntity productCategory) {
        return ProductEntity.builder()
                .name("Bebida 2")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategory(productCategory)
                .restaurant(restaurantEntity)
                .build();
    }

    public Product getProduct(Restaurant restaurant, ProductCategory productCategory) {
        return Product.builder()
                .name("Bebida")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategoryId(restaurant.getId())
                .restaurantId(productCategory.getId())
                .build();
    }

    public Product getProduct1(Restaurant restaurant, ProductCategory productCategory) {
        return Product.builder()
                .name("Bebida 1")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategoryId(restaurant.getId())
                .restaurantId(productCategory.getId())
                .build();
    }

    public Product getProduct2(Restaurant restaurant, ProductCategory productCategory) {
        return Product.builder()
                .name("Bebida 2")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategoryId(restaurant.getId())
                .restaurantId(productCategory.getId())
                .build();
    }


    public ProductCategoryEntity getProductCategoryEntity() {
        return ProductCategoryEntity.builder()
                .name("Bebida")
                .build();
    }

    public ProductCategory getProductCategory() {
        return ProductCategory.builder()
                .name("Bebida")
                .build();
    }

    public ProductCategory getProductCategoryTo260() {
        return ProductCategory.builder()
                .name("a".repeat(260))
                .build();
    }
}