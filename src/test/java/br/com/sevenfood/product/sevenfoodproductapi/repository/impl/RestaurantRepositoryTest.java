package br.com.sevenfood.product.sevenfoodproductapi.repository.impl;

import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class RestaurantRepositoryTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private RestaurantEntity restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new RestaurantEntity();
        restaurant.setId(1L);
        restaurant.setCnpj("12345678901234");
        restaurant.setName("Coca Cola Beverage");
    }

    @Test
    void testFindByCnpj() {
        when(restaurantRepository.findByCnpj("12345678901234")).thenReturn(Optional.of(restaurant));

        Optional<RestaurantEntity> found = restaurantRepository.findByCnpj("12345678901234");

        assertTrue(found.isPresent());
        Assertions.assertEquals("12345678901234",found.get().getCnpj());
    }

    @Test
    void testFindByName() {
        when(restaurantRepository.findByName("Coca Cola Beverage")).thenReturn(Optional.of(restaurant));

        Optional<RestaurantEntity> found = restaurantRepository.findByName("Coca Cola Beverage");

        assertTrue(found.isPresent());
        Assertions.assertEquals("Coca Cola Beverage",found.get().getName());
    }
}
