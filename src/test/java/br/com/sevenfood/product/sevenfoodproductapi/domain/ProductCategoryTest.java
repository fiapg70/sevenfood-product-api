package br.com.sevenfood.product.sevenfoodproductapi.domain;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductCategoryTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(1L);
        productCategory.setName("Test Name");

        // Assert
        assertEquals(1L, productCategory.getId());
        assertEquals("Test Name", productCategory.getName());
    }

    @Test
    void testToString() {
        // Arrange
        ProductCategory productCategory = ProductCategory.builder()
                .id(1L)
                .name("Test Name")
                .build();

        // Assert
        assertEquals("ProductCategory(id=1, name=Test Name)", productCategory.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        ProductCategory productCategory1 = ProductCategory.builder()
                .id(1L)
                .name("Test Name")
                .build();

        ProductCategory productCategory2 = ProductCategory.builder()
                .id(1L)
                .name("Test Name")
                .build();

        // Assert
        assertEquals(productCategory1, productCategory2);
        assertEquals(productCategory1, productCategory2);
    }

    @Test
    void testUpdate() {
        // Arrange
        ProductCategory originalProductCategory = ProductCategory.builder()
                .id(1L)
                .name("Original Name")
                .build();

        ProductCategory newProductCategory = ProductCategory.builder()
                .name("New Name")
                .build();

        // Act
        originalProductCategory.update(2L, newProductCategory);

        // Assert
        assertEquals(2L, originalProductCategory.getId());
        assertEquals("New Name", originalProductCategory.getName());
    }
}
