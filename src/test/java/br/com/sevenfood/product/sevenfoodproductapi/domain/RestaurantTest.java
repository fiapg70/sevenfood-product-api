package br.com.sevenfood.product.sevenfoodproductapi.domain;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RestaurantTest {

    @Test
    void testUpdate() {
        // Arrange
        Restaurant originalRestaurant = Restaurant.builder()
                .id(1L)
                .name("Original Name")
                .cnpj("Original CNPJ")
                .build();

        Restaurant newRestaurant = Restaurant.builder()
                .name("New Name")
                .cnpj("New CNPJ")
                .build();

        // Act
        originalRestaurant.update(2L, newRestaurant);

        // Assert
        assertThat(originalRestaurant.getId()).isEqualTo(2L);
        assertThat(originalRestaurant.getName()).isEqualTo("New Name");
        assertThat(originalRestaurant.getCnpj()).isEqualTo("New CNPJ");
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Name");
        restaurant.setCnpj("Test CNPJ");

        // Assert
        assertThat(restaurant.getId()).isEqualTo(1L);
        assertThat(restaurant.getName()).isEqualTo("Test Name");
        assertThat(restaurant.getCnpj()).isEqualTo("Test CNPJ");
    }

    @Test
    void testToString() {
        // Arrange
        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Name")
                .cnpj("Test CNPJ")
                .build();

        // Assert
        assertThat(restaurant).hasToString("Restaurant(id=1, name=Test Name, cnpj=Test CNPJ)");
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Restaurant restaurant1 = Restaurant.builder()
                .id(1L)
                .name("Test Name")
                .cnpj("Test CNPJ")
                .build();

        Restaurant restaurant2 = Restaurant.builder()
                .id(1L)
                .name("Test Name")
                .cnpj("Test CNPJ")
                .build();

        // Assert
        assertThat(restaurant1).isEqualTo(restaurant2);
        assertThat(restaurant1).hasSameHashCodeAs(restaurant2);
    }
}
