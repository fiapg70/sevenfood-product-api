package br.com.sevenfood.product.sevenfoodproductapi.entity;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantEntityTest {

    @Test
    void testUpdateNew() {
        RestaurantEntity restaurant = new RestaurantEntity();
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName("Updated Restaurant");
        newRestaurant.setCnpj("98.765.432/0001-11");

        restaurant.update(2L, newRestaurant);

        assertThat(restaurant.getId()).isEqualTo(2L);
        assertThat(restaurant.getName()).isEqualTo("Updated Restaurant");
        assertThat(restaurant.getCnpj()).isEqualTo("98.765.432/0001-11");
    }

    @Test
    void testUpdate() {
        // Arrange
        Long id = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Updated Name");
        restaurant.setCnpj("Updated CNPJ");

        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(2L);
        entity.setName("Old Name");
        entity.setCnpj("Old CNPJ");

        // Act
        entity.update(id, restaurant);

        // Assert
        assertEquals(id, entity.getId());
        assertEquals("Updated Name", entity.getName());
        assertEquals("Updated CNPJ", entity.getCnpj());
    }


    @Test
    void testGettersAndSetters() {
        // Arrange
        Long id = 1L;
        String name = "Test Restaurant";
        String cnpj = "12.345.678/0001-99";
        RestaurantEntity restaurant = new RestaurantEntity();

        // Act
        restaurant.setId(id);
        restaurant.setName(name);
        restaurant.setCnpj(cnpj);

        // Assert
        assertEquals(id, restaurant.getId());
        assertEquals(name, restaurant.getName());
        assertEquals(cnpj, restaurant.getCnpj());
    }

    @Test
    void testToString() {
        // Arrange
        Long id = 1L;
        String name = "Test Restaurant";
        String cnpj = "12.345.678/0001-99";
        RestaurantEntity restaurant = RestaurantEntity.builder()
                .id(id)
                .name(name)
                .cnpj(cnpj)
                .build();

        // Act
        String expected = "RestaurantEntity(id=1, name=Test Restaurant, cnpj=12.345.678/0001-99)";

        // Assert
        assertEquals(expected, restaurant.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
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

        // Assert
        assertEquals(restaurant1, restaurant2);
        assertEquals(restaurant1.hashCode(), restaurant2.hashCode());
    }
}
