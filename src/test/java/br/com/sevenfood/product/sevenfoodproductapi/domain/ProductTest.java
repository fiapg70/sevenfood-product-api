package br.com.sevenfood.product.sevenfoodproductapi.domain;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductTest {

    @Test
    void testGettersAndSetters() {
        Product product = new Product();
        product.setId(1L);
        product.setCode("CODE123");
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(new BigDecimal("9.99"));
        product.setPic("pic.jpg");
        product.setProductCategoryId(2L);
        product.setRestaurantId(3L);

        assertEquals(1L, product.getId());
        assertEquals("CODE123", product.getCode());
        assertEquals("Product Name", product.getName());
        assertEquals("Product Description", product.getDescription());
        assertEquals(new BigDecimal("9.99"), product.getPrice());
        assertEquals("pic.jpg", product.getPic());
        assertEquals(2L, product.getProductCategoryId());
        assertEquals(3L, product.getRestaurantId());
    }

    @Test
    void testBuilder() {
        Product product = Product.builder()
                .id(1L)
                .code("CODE123")
                .name("Product Name")
                .description("Product Description")
                .price(new BigDecimal("9.99"))
                .pic("pic.jpg")
                .productCategoryId(2L)
                .restaurantId(3L)
                .build();

        assertEquals(1L, product.getId());
        assertEquals("CODE123", product.getCode());
        assertEquals("Product Name", product.getName());
        assertEquals("Product Description", product.getDescription());
        assertEquals(new BigDecimal("9.99"), product.getPrice());
        assertEquals("pic.jpg", product.getPic());
        assertEquals(2L, product.getProductCategoryId());
        assertEquals(3L, product.getRestaurantId());
    }

    @Test
    void testUpdate() {
        Product product = new Product();
        product.setId(1L);
        product.setCode("CODE123");
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(new BigDecimal("9.99"));
        product.setPic("pic.jpg");
        product.setProductCategoryId(2L);
        product.setRestaurantId(3L);

        Product newProduct = new Product();
        newProduct.setCode("NEWCODE");
        newProduct.setName("New Product Name");
        newProduct.setDescription("New Product Description");
        newProduct.setPrice(new BigDecimal("19.99"));
        newProduct.setPic("newpic.jpg");
        newProduct.setProductCategoryId(4L);
        newProduct.setRestaurantId(5L);

        product.update(2L, newProduct);

        assertEquals(2L, product.getId());
        assertEquals("NEWCODE", product.getCode());
        assertEquals("New Product Name", product.getName());
        assertEquals("New Product Description", product.getDescription());
        assertEquals(new BigDecimal("19.99"), product.getPrice());
        assertEquals("newpic.jpg", product.getPic());
        assertEquals(4L, product.getProductCategoryId());
        assertEquals(5L, product.getRestaurantId());
    }

    @Test
    void testNoArgsConstructor() {
        Product product = new Product();
        assertNotNull(product);
    }

    @Test
    void testAllArgsConstructor() {
        Product product = new Product(1L, "CODE123", "Product Name", "Product Description", new BigDecimal("9.99"), "pic.jpg", 2L, 3L);
        assertEquals(1L, product.getId());
        assertEquals("CODE123", product.getCode());
        assertEquals("Product Name", product.getName());
        assertEquals("Product Description", product.getDescription());
        assertEquals(new BigDecimal("9.99"), product.getPrice());
        assertEquals("pic.jpg", product.getPic());
        assertEquals(2L, product.getProductCategoryId());
        assertEquals(3L, product.getRestaurantId());
    }
}