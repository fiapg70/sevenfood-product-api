package br.com.sevenfood.product.sevenfoodproductapi.domain;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantTest {

    @Test
    void testGettersAndSetters() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setCnpj("123456789");

        assertThat(restaurant.getId()).isEqualTo(1L);
        assertThat(restaurant.getName()).isEqualTo("Test Restaurant");
        assertThat(restaurant.getCnpj()).isEqualTo("123456789");
    }

    @Test
    void testEqualsAndHashCode() {
        Restaurant restaurant1 = new Restaurant(1L, "Test Restaurant", "123456789");
        Restaurant restaurant2 = new Restaurant(1L, "Test Restaurant", "123456789");

        assertThat(restaurant1).isEqualTo(restaurant2);
        assertThat(restaurant1.hashCode()).hasSameHashCodeAs(restaurant2.hashCode());
    }

    @Test
    void testToString() {
        Restaurant restaurant = new Restaurant(1L, "Test Restaurant", "123456789");
        String expectedString = "Restaurant(id=1, name=Test Restaurant, cnpj=123456789)";

        assertThat(restaurant.toString()).hasToString(expectedString);
    }

    @Test
    void testBuilder() {
        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .cnpj("123456789")
                .build();

        assertThat(restaurant.getId()).isEqualTo(1L);
        assertThat(restaurant.getName()).isEqualTo("Test Restaurant");
        assertThat(restaurant.getCnpj()).isEqualTo("123456789");
    }

    @Test
    void testUpdate() {
        Restaurant restaurant = new Restaurant(1L, "Old Name", "987654321");
        Restaurant newRestaurant = new Restaurant(null, "New Name", "123456789");

        restaurant.update(2L, newRestaurant);

        assertThat(restaurant.getId()).isEqualTo(2L);
        assertThat(restaurant.getName()).isEqualTo("New Name");
        assertThat(restaurant.getCnpj()).isEqualTo("123456789");
    }
}
