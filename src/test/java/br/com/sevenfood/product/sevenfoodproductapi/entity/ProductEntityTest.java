package br.com.sevenfood.product.sevenfoodproductapi.entity;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductEntityTest {

    @Test
    void testUpdate() {
        // Arrange
        Long id = 1L;
        Product product = new Product();
        product.setName("Updated Name");
        product.setPic("Updated Pic");
        product.setDescription("Updated Description");
        product.setPrice(new BigDecimal("99.99"));

        ProductEntity entity = new ProductEntity();
        entity.setId(2L);
        entity.setName("Old Name");
        entity.setPic("Old Pic");
        entity.setDescription("Old Description");
        entity.setPrice(new BigDecimal("49.99"));

        // Act
        entity.update(id, entity);

        // Assert
        assertEquals(id, entity.getId());
        assertEquals("Old Name", entity.getName());
        assertEquals("Old Pic", entity.getPic());
        assertEquals("Old Description", entity.getDescription());
        assertEquals(new BigDecimal("49.99"), entity.getPrice());
    }

    @Test
    void testGettersAndSetters() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setName("Old Name");
        entity.setPic("Old Pic");
        entity.setDescription("Old Description");
        entity.setPrice(new BigDecimal("49.99"));

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Old Name");
        assertThat(entity.getPic()).isEqualTo("Old Pic");
    }

    @Test
    void testToString() {
        ProductCategoryEntity productCategory = new ProductCategoryEntity();
        productCategory.setId(1L);
        productCategory.setName("Bebida");

        RestaurantEntity restaurant = RestaurantEntity.builder()
                .id(1L)
                .name("Test Restaurant")
                .cnpj("12.345.678/0001-99")
                .build();

        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setCode("d7d19a26-846f-4808-818b-ffc3495be7bb");
        product.setName("Old Name");
        product.setPic("Old Pic");
        product.setDescription("Old Description");
        product.setPrice(new BigDecimal("49.99"));
        product.setProductCategory(productCategory);
        product.setRestaurant(restaurant);

        String expected = "ProductEntity(id=1, code=d7d19a26-846f-4808-818b-ffc3495be7bb, name=Old Name, pic=Old Pic, description=Old Description, price=49.99, productCategory=ProductCategoryEntity(id=1, name=Bebida), restaurant=RestaurantEntity(id=1, name=Test Restaurant, cnpj=12.345.678/0001-99))";
        assertThat(product).hasToString(expected);
    }

    @Test
    void testEqualsAndHashCode() {
        ProductEntity product1 = ProductEntity.builder()
                .id(1L)
                .name("Old Name")
                .pic("Old Pic")
                .description("Old Description")
                .build();

        ProductEntity product2 = ProductEntity.builder()
                .id(1L)
                .name("Old Name")
                .pic("Old Pic")
                .description("Old Description")
                .build();

        assertThat(product2).isEqualTo(product2);
        assertThat(product1).hasSameHashCodeAs(product2);
    }
}
