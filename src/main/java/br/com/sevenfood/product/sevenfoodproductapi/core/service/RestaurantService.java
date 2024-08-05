package br.com.sevenfood.product.sevenfoodproductapi.core.service;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.exception.ResourceFoundException;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.restaurant.*;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.out.RestaurantRepositoryPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RestaurantService implements CreateRestaurantPort, UpdateRestaurantPort, FindByIdRestaurantPort, FindRestaurantsPort, DeleteRestaurantPort {

    private final RestaurantRepositoryPort restaurantRepository;

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant update(Long id, Restaurant restaurant) {
        Restaurant resultById = findById(id);
        if (resultById != null) {
            resultById.update(id, restaurant);

            return restaurantRepository.save(resultById);
        }

        return null;
    }

    @Override
    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public List<Restaurant> findAll() {
       return restaurantRepository.findAll();
    }

    @Override
    public boolean remove(Long id) {
        try {
            Restaurant restaurantById = findById(id);
            if (restaurantById == null) {
                throw new ResourceFoundException("Restaurant not found");
            }

            restaurantRepository.remove(id);
            return Boolean.TRUE;
        } catch (ResourceFoundException e) {
            log.error("Erro ao remover restaurante: {}", e.getMessage());
            return Boolean.FALSE;
        }
    }
}
