package br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.restaurant;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;

public interface CreateRestaurantPort {
    Restaurant save(Restaurant restaurant);
}
