package br.com.sevenfood.product.sevenfoodproductapi.domain;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProductTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setCode("V$");
        product.setName("Test Name");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("49.99"));
        product.setPic("Test Pic");
        product.setProductCategoryId(2L);
        product.setRestaurantId(3L);

        // Assert
        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getCode()).isEqualTo("V$");
        assertThat(product.getName()).isEqualTo("Test Name");
        assertThat(product.getDescription()).isEqualTo("Test Description");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("49.99"));
        assertThat(product.getPic()).isEqualTo("Test Pic");
        assertThat(product.getProductCategoryId()).isEqualTo(2L);
        assertThat(product.getRestaurantId()).isEqualTo(3L);
    }

    @Test
    void testToString() {
        // Arrange
        Product product = Product.builder()
                .id(1L)
                .code("V$")
                .name("Test Name")
                .description("Test Description")
                .price(new BigDecimal("49.99"))
                .pic("Test Pic")
                .productCategoryId(2L)
                .restaurantId(3L)
                .build();

        // Assert
        assertThat(product).hasToString("Product(id=1, code=V$, name=Test Name, description=Test Description, price=49.99, pic=Test Pic, productCategoryId=2, restaurantId=3)");
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Product product1 = Product.builder()
                .id(1L)
                .code("V$")
                .name("Test Name")
                .description("Test Description")
                .price(new BigDecimal("49.99"))
                .pic("Test Pic")
                .productCategoryId(2L)
                .restaurantId(3L)
                .build();

        Product product2 = Product.builder()
                .id(1L)
                .code("V$")
                .name("Test Name")
                .description("Test Description")
                .price(new BigDecimal("49.99"))
                .pic("Test Pic")
                .productCategoryId(2L)
                .restaurantId(3L)
                .build();

        // Assert
        assertThat(product1).isEqualTo(product2);
        assertThat(product1).hasSameHashCodeAs(product2);
    }

    @Test
    void testUpdate() {
        // Arrange
        Product originalProduct = Product.builder()
                .id(1L)
                .code("V$")
                .name("Original Name")
                .description("Original Description")
                .price(new BigDecimal("49.99"))
                .pic("Original Pic")
                .productCategoryId(2L)
                .restaurantId(3L)
                .build();

        Product newProduct = Product.builder()
                .code("New Code")
                .name("New Name")
                .description("New Description")
                .price(new BigDecimal("59.99"))
                .pic("New Pic")
                .productCategoryId(4L)
                .restaurantId(5L)
                .build();

        // Act
        originalProduct.update(2L, newProduct);

        // Assert
        assertThat(originalProduct.getId()).isEqualTo(2L);
        assertThat(originalProduct.getCode()).isEqualTo("New Code");
        assertThat(originalProduct.getName()).isEqualTo("New Name");
        assertThat(originalProduct.getDescription()).isEqualTo("New Description");
        assertThat(originalProduct.getPrice()).isEqualTo(new BigDecimal("59.99"));
        assertThat(originalProduct.getPic()).isEqualTo("New Pic");
        assertThat(originalProduct.getProductCategoryId()).isEqualTo(4L);
        assertThat(originalProduct.getRestaurantId()).isEqualTo(5L);
    }
}