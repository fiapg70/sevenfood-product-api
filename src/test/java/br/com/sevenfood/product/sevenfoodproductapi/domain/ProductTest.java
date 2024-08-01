package br.com.sevenfood.product.sevenfoodproductapi.domain;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductTest {

    @Test
    void testGettersAndSetters() {
        Product product = new Product();

        product.setId(1L);
        product.setCode("V$123");
        product.setName("Pizza");
        product.setDescription("Delicious pizza with cheese");
        product.setPrice(BigDecimal.valueOf(19.99));
        product.setPic("pic.jpg");
        product.setProductCategoryId(2L);
        product.setRestaurantId(3L);

        assertEquals(1L, product.getId());
        assertEquals("V$123", product.getCode());
        assertEquals("Pizza", product.getName());
        assertEquals("Delicious pizza with cheese", product.getDescription());
        assertEquals(BigDecimal.valueOf(19.99), product.getPrice());
        assertEquals("pic.jpg", product.getPic());
        assertEquals(2L, product.getProductCategoryId());
        assertEquals(3L, product.getRestaurantId());
    }

    @Test
    void testEqualsAndHashCode() {
        Product product1 = new Product(1L, "V$123", "Pizza", "Delicious pizza", BigDecimal.valueOf(19.99), "pic.jpg", 2L, 3L);
        Product product2 = new Product(1L, "V$123", "Pizza", "Delicious pizza", BigDecimal.valueOf(19.99), "pic.jpg", 2L, 3L);

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void testToString() {
        Product product = new Product(1L, "V$123", "Pizza", "Delicious pizza", BigDecimal.valueOf(19.99), "pic.jpg", 2L, 3L);

        String expectedString = "Product(id=1, code=V$123, name=Pizza, description=Delicious pizza, price=19.99, pic=pic.jpg, productCategoryId=2, restaurantId=3)";
        assertEquals(expectedString, product.toString());
    }

    @Test
    void testUpdate() {
        Product existingProduct = new Product(1L, "V$123", "Pizza", "Delicious pizza", BigDecimal.valueOf(19.99), "pic.jpg", 2L, 3L);
        Product newProduct = new Product(null, "V$456", "Burger", "Juicy burger", BigDecimal.valueOf(9.99), "burger.jpg", 4L, 5L);

        existingProduct.update(1L, newProduct);

        assertEquals(1L, existingProduct.getId());
        assertEquals("V$456", existingProduct.getCode());
        assertEquals("Burger", existingProduct.getName());
        assertEquals("Juicy burger", existingProduct.getDescription());
        assertEquals(BigDecimal.valueOf(9.99), existingProduct.getPrice());
        assertEquals("burger.jpg", existingProduct.getPic());
        assertEquals(4L, existingProduct.getProductCategoryId());
        assertEquals(5L, existingProduct.getRestaurantId());
    }
}