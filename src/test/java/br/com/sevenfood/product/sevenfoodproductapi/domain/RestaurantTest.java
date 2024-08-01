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
        restaurant.setName("Seven Food");
        restaurant.setCnpj("02.365.347/0001-63");

        assertEquals(1L, restaurant.getId());
        assertEquals("Seven Food", restaurant.getName());
        assertEquals("02.365.347/0001-63", restaurant.getCnpj());
    }

    @Test
    void testEqualsAndHashCode() {
        Restaurant restaurant1 = new Restaurant(1L, "Seven Food", "02.365.347/0001-63");
        Restaurant restaurant2 = new Restaurant(1L, "Seven Food", "02.365.347/0001-63");

        assertEquals(restaurant1, restaurant2);
        assertEquals(restaurant1.hashCode(), restaurant2.hashCode());
    }

    @Test
    void testToString() {
        Restaurant restaurant = new Restaurant(1L, "Seven Food", "02.365.347/0001-63");

        String expectedString = "Restaurant(id=1, name=Seven Food, cnpj=02.365.347/0001-63)";
        assertEquals(expectedString, restaurant.toString());
    }

    @Test
    void testUpdate() {
        Restaurant existingRestaurant = new Restaurant(1L, "Seven Food", "02.365.347/0001-63");
        Restaurant newRestaurant = new Restaurant(null, "Eight Food", "03.456.789/0002-54");

        existingRestaurant.update(2L, newRestaurant);

        assertEquals(2L, existingRestaurant.getId());
        assertEquals("Eight Food", existingRestaurant.getName());
        assertEquals("03.456.789/0002-54", existingRestaurant.getCnpj());
    }
}
