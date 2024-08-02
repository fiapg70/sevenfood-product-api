package br.com.sevenfood.product.sevenfoodproductapi.application.mapper;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.ProductMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperTest {

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void testFromModelToEntity() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("9.99"));
        product.setPic("test-pic.jpg");
        product.setProductCategoryId(2L);
        product.setRestaurantId(3L);
        product.setCode("TEST_CODE");

        ProductEntity productEntity = mapper.fromModelTpEntity(product);

        assertEquals(product.getName(), productEntity.getName());
        assertEquals(product.getDescription(), productEntity.getDescription());
        assertEquals(product.getPrice(), productEntity.getPrice());
        assertEquals(product.getPic(), productEntity.getPic());
        assertEquals(product.getProductCategoryId(), productEntity.getProductCategory().getId());
        assertEquals(product.getRestaurantId(), productEntity.getRestaurant().getId());
    }

    @Test
    void testFromEntityToModel() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Test Product");
        productEntity.setDescription("Test Description");
        productEntity.setPrice(new BigDecimal("9.99"));
        productEntity.setPic("test-pic.jpg");
        ProductCategoryEntity productCategory = new ProductCategoryEntity();
        productCategory.setId(2L);
        productEntity.setProductCategory(productCategory);
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(3L);
        productEntity.setRestaurant(restaurant);
        productEntity.setCode("TEST_CODE");

        Product product = mapper.fromEntityToModel(productEntity);

        assertEquals(productEntity.getId(), product.getId());
        assertEquals(productEntity.getName(), product.getName());
        assertEquals(productEntity.getDescription(), product.getDescription());
        assertEquals(productEntity.getPrice(), product.getPrice());
        assertEquals(productEntity.getPic(), product.getPic());
        assertEquals(productEntity.getProductCategory().getId(), product.getProductCategoryId());
        assertEquals(productEntity.getRestaurant().getId(), product.getRestaurantId());
        assertEquals(productEntity.getCode(), product.getCode());
    }

    @Test
     void testMapList() {
        ProductEntity productEntity1 = new ProductEntity();
        productEntity1.setId(1L);
        productEntity1.setName("Test Product 1");
        productEntity1.setDescription("Test Description 1");
        productEntity1.setPrice(new BigDecimal("9.99"));
        productEntity1.setPic("test-pic1.jpg");
        ProductCategoryEntity productCategory1 = new ProductCategoryEntity();
        productCategory1.setId(2L);
        productEntity1.setProductCategory(productCategory1);
        RestaurantEntity restaurant1 = new RestaurantEntity();
        restaurant1.setId(3L);
        productEntity1.setRestaurant(restaurant1);
        productEntity1.setCode("TEST_CODE_1");

        ProductEntity productEntity2 = new ProductEntity();
        productEntity2.setId(2L);
        productEntity2.setName("Test Product 2");
        productEntity2.setDescription("Test Description 2");
        productEntity2.setPrice(new BigDecimal("19.99"));
        productEntity2.setPic("test-pic2.jpg");
        ProductCategoryEntity productCategory2 = new ProductCategoryEntity();
        productCategory2.setId(3L);
        productEntity2.setProductCategory(productCategory2);
        RestaurantEntity restaurant2 = new RestaurantEntity();
        restaurant2.setId(4L);
        productEntity2.setRestaurant(restaurant2);
        productEntity2.setCode("TEST_CODE_2");

        List<ProductEntity> productEntities = Arrays.asList(productEntity1, productEntity2);

        List<Product> products = mapper.map(productEntities);

        assertEquals(2, products.size());

        Product product1 = products.get(0);
        assertEquals(productEntity1.getId(), product1.getId());
        assertEquals(productEntity1.getName(), product1.getName());
        assertEquals(productEntity1.getDescription(), product1.getDescription());
        assertEquals(productEntity1.getPrice(), product1.getPrice());
        assertEquals(productEntity1.getPic(), product1.getPic());
        assertEquals(productEntity1.getProductCategory().getId(), product1.getProductCategoryId());
        assertEquals(productEntity1.getRestaurant().getId(), product1.getRestaurantId());
        assertEquals(productEntity1.getCode(), product1.getCode());

        Product product2 = products.get(1);
        assertEquals(productEntity2.getId(), product2.getId());
        assertEquals(productEntity2.getName(), product2.getName());
        assertEquals(productEntity2.getDescription(), product2.getDescription());
        assertEquals(productEntity2.getPrice(), product2.getPrice());
        assertEquals(productEntity2.getPic(), product2.getPic());
        assertEquals(productEntity2.getProductCategory().getId(), product2.getProductCategoryId());
        assertEquals(productEntity2.getRestaurant().getId(), product2.getRestaurantId());
        assertEquals(productEntity2.getCode(), product2.getCode());
    }
}
