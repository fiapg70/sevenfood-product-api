package br.com.sevenfood.product.sevenfoodproductapi.application.mapper;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.RestaurantMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestaurantMapperTest {

    private RestaurantMapper mapper = Mappers.getMapper(RestaurantMapper.class);

    @Test
    void testFromModelToEntity() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setCnpj("00.000.000/0001-00");

        RestaurantEntity entity = mapper.fromModelToEntity(restaurant);

        assertNotNull(entity);
        assertEquals(restaurant.getName(), entity.getName());
        assertEquals(restaurant.getCnpj(), entity.getCnpj());
    }

    @Test
    void testFromEntityToModel() {
        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(1L);
        entity.setName("Test Restaurant");
        entity.setCnpj("00.000.000/0001-00");

        Restaurant restaurant = mapper.fromEntityToModel(entity);

        assertNotNull(restaurant);
        assertEquals(entity.getId(), restaurant.getId());
        assertEquals(entity.getName(), restaurant.getName());
        assertEquals(entity.getCnpj(), restaurant.getCnpj());
    }

    @Test
    void testMapEntityListToModelList() {
        RestaurantEntity entity1 = new RestaurantEntity();
        entity1.setId(1L);
        entity1.setName("Test Restaurant 1");
        entity1.setCnpj("00.000.000/0001-01");

        RestaurantEntity entity2 = new RestaurantEntity();
        entity2.setId(2L);
        entity2.setName("Test Restaurant 2");
        entity2.setCnpj("00.000.000/0001-02");

        List<RestaurantEntity> entityList = new ArrayList<>();
        entityList.add(entity1);
        entityList.add(entity2);

        List<Restaurant> restaurantList = mapper.map(entityList);

        assertNotNull(restaurantList);
        assertEquals(2, restaurantList.size());
        assertEquals(entity1.getId(), restaurantList.get(0).getId());
        assertEquals(entity1.getName(), restaurantList.get(0).getName());
        assertEquals(entity1.getCnpj(), restaurantList.get(0).getCnpj());
        assertEquals(entity2.getId(), restaurantList.get(1).getId());
        assertEquals(entity2.getName(), restaurantList.get(1).getName());
        assertEquals(entity2.getCnpj(), restaurantList.get(1).getCnpj());
    }
}
