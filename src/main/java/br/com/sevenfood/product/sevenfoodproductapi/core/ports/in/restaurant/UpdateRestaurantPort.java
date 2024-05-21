package br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.restaurant;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;

public interface UpdateRestaurantPort {
    Restaurant update(Long id, Restaurant restaurant);
}
