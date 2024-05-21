package br.com.sevenfood.product.sevenfoodproductapi.core.ports.out;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;

import java.util.List;

public interface RestaurantRepositoryPort {
    Restaurant save(Restaurant restaurant);
    boolean remove(Long id);
    Restaurant findById(Long id);
    List<Restaurant> findAll();
    Restaurant update(Long id, Restaurant restaurant);
}
