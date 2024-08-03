package br.com.sevenfood.product.sevenfoodproductapi.entity;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductCategoryEntityTest {

    @Test
    void testUpdate() {
        // Arrange
        Long id = 1L;
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("Updated Name");

        ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();
        productCategoryEntity.setId(2L);
        productCategoryEntity.setName("Old Name");

        // Act
        productCategoryEntity.update(id, productCategoryEntity);

        // Assert
        assertEquals(id, productCategoryEntity.getId());
        assertEquals("Updated Name", productCategoryEntity.getName());
    }

    @Test
    void testGettersAndSetters() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(1L);
        productCategory.setName("Updated Name");

        assertThat(productCategory.getId()).isEqualTo(1L);
        assertThat(productCategory.getName()).isEqualTo("Updated Name");
    }

    @Test
    void testToString() {
        ProductCategory productCategory = ProductCategory.builder()
                .id(1L)
                .name("Updated Name")
                .build();

        String expected = "ProductCategory(id=1, name=Updated Name)";
        assertEquals(productCategory.toString(), expected);
    }

    @Test
    void testEqualsAndHashCode() {
        RestaurantEntity restaurant1 = RestaurantEntity.builder()
                .id(1L)
                .name("Test Restaurant")
                .cnpj("12.345.678/0001-99")
                .build();

        RestaurantEntity restaurant2 = RestaurantEntity.builder()
                .id(1L)
                .name("Test Restaurant")
                .cnpj("12.345.678/0001-99")
                .build();

        assertEquals(restaurant1, restaurant2);
        assertEquals(restaurant1.hashCode(), restaurant2.hashCode());
    }
}
