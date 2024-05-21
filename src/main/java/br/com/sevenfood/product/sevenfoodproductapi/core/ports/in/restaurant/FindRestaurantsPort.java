package br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.restaurant;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;

import java.util.List;

public interface FindRestaurantsPort {
    List<Restaurant> findAll();
}
