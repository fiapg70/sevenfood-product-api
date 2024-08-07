package br.com.sevenfood.product.sevenfoodproductapi.application.database.repository;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.exception.ResourceFoundException;
import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.RestaurantMapper;
import br.com.sevenfood.product.sevenfoodproductapi.commons.exception.CNPJFoundException;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.out.RestaurantRepositoryPort;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RestaurantRepositoryAdapter implements RestaurantRepositoryPort {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = restaurantMapper.fromModelToEntity(restaurant);
        Optional<RestaurantEntity> byCnpj = restaurantRepository.findByCnpj(restaurantEntity.getCnpj());

        if (byCnpj.isPresent()) {
            throw new CNPJFoundException("CNPJ already registered");
        }

        RestaurantEntity saved = restaurantRepository.save(restaurantEntity);
        if (saved.getName() == null) {
            throw new ResourceFoundException("Erro ao salvar produto no repositorio");
        }
        return restaurantMapper.fromEntityToModel(saved);
    }

    @Override
    public boolean remove(Long id) {
         try {
            restaurantRepository.deleteById(id);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public Restaurant findById(Long id) {
        Optional<RestaurantEntity> buRestaurant = restaurantRepository.findById(id);
        if (buRestaurant.isPresent()) {
            return restaurantMapper.fromEntityToModel(buRestaurant.get());
        }
        return null;
    }

    @Override
    public List<Restaurant> findAll() {
        List<RestaurantEntity> all = restaurantRepository.findAll();
        return restaurantMapper.map(all);
    }

    @Override
    public Restaurant update(Long id, Restaurant restaurant) {
        Optional<RestaurantEntity> resultById = restaurantRepository.findById(id);
        if (resultById.isPresent()) {

            RestaurantEntity restaurantToChange = resultById.get();
            restaurantToChange.update(id, restaurantToChange);

            return restaurantMapper.fromEntityToModel(restaurantRepository.save(restaurantToChange));
        }

        return null;
    }
}
