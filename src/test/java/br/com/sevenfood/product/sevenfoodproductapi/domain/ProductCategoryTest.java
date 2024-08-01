package br.com.sevenfood.product.sevenfoodproductapi.domain;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductCategoryTest {

    @Test
    void testGettersAndSetters() {
        ProductCategory productCategory = new ProductCategory();

        productCategory.setId(1L);
        productCategory.setName("Beverages");

        assertEquals(1L, productCategory.getId());
        assertEquals("Beverages", productCategory.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        ProductCategory category1 = new ProductCategory(1L, "Beverages");
        ProductCategory category2 = new ProductCategory(1L, "Beverages");

        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());
    }

    @Test
    void testToString() {
        ProductCategory productCategory = new ProductCategory(1L, "Beverages");

        String expectedString = "ProductCategory(id=1, name=Beverages)";
        assertEquals(expectedString, productCategory.toString());
    }

    @Test
    void testUpdate() {
        ProductCategory category1 = new ProductCategory(1L, "Beverages");
        ProductCategory category2 = new ProductCategory();
        category2.update(2L, category1);

        assertEquals(2L, category2.getId());
        assertEquals("Beverages", category2.getName());
    }
}
