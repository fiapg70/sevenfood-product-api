package br.com.sevenfood.product.sevenfoodproductapi.domain;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductCategoryTest {

    private ProductCategory productCategory;
    private ProductCategory anotherProductCategory;

    @BeforeEach
    void setUp() {
        productCategory = ProductCategory.builder()
                .id(1L)
                .name("Beverages")
                .build();

        anotherProductCategory = ProductCategory.builder()
                .id(2L)
                .name("Snacks")
                .build();
    }

    @Test
    void testGetters() {
        assertEquals(1L, productCategory.getId());
        assertEquals("Beverages", productCategory.getName());
    }

    @Test
    void testSetters() {
        productCategory.setId(2L);
        productCategory.setName("Snacks");

        assertEquals(2L, productCategory.getId());
        assertEquals("Snacks", productCategory.getName());
    }

    @Test
    void testEquals() {
        ProductCategory copy = ProductCategory.builder()
                .id(1L)
                .name("Beverages")
                .build();

        assertEquals(productCategory, copy);
    }

    @Test
    void testHashCode() {
        ProductCategory copy = ProductCategory.builder()
                .id(1L)
                .name("Beverages")
                .build();

        assertEquals(productCategory.hashCode(), copy.hashCode());
    }

    @Test
    void testToString() {
        String expected = "ProductCategory(id=1, name=Beverages)";
        assertEquals(expected, productCategory.toString());
    }

    @Test
    void testUpdate() {
        productCategory.update(3L, anotherProductCategory);

        assertEquals(3L, productCategory.getId());
        assertEquals("Snacks", productCategory.getName());
    }

    @Test
    void testNoArgsConstructor() {
        ProductCategory newProductCategory = new ProductCategory();
        assertNotNull(newProductCategory);
    }

    @Test
    void testAllArgsConstructor() {
        ProductCategory newProductCategory = new ProductCategory(4L, "Frozen Foods");
        assertEquals(4L, newProductCategory.getId());
        assertEquals("Frozen Foods", newProductCategory.getName());
    }
}
