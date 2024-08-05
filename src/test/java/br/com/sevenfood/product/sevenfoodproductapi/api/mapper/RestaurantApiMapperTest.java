package br.com.sevenfood.product.sevenfoodproductapi.api.mapper;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.RestaurantRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.response.RestaurantResponse;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.mapper.RestaurantApiMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestaurantApiMapperTest {

    private final RestaurantApiMapper mapper = Mappers.getMapper(RestaurantApiMapper.class);

    @Test
    void testFromRequest() {
        RestaurantRequest request = new RestaurantRequest();
        request.setName("Test Restaurant");
        request.setCnpj("12345678901234");

        Restaurant restaurant = mapper.fromRequest(request);

        assertNotNull(restaurant);
        assertEquals(request.getName(), restaurant.getName());
        assertEquals(request.getCnpj(), restaurant.getCnpj());
    }

    @Test
    void testFromEntity() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setCnpj("12345678901234");

        RestaurantResponse response = mapper.fromEntity(restaurant);

        assertNotNull(response);
        assertEquals(restaurant.getId(), response.getId());
        assertEquals(restaurant.getName(), response.getName());
        assertEquals(restaurant.getCnpj(), response.getCnpj());
    }

    @Test
    void testMapList() {
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setName("Restaurant 1");
        restaurant1.setCnpj("11111111111111");

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setId(2L);
        restaurant2.setName("Restaurant 2");
        restaurant2.setCnpj("22222222222222");

        List<Restaurant> restaurantList = Arrays.asList(restaurant1, restaurant2);
        List<RestaurantResponse> responseList = mapper.map(restaurantList);

        assertNotNull(responseList);
        assertEquals(2, responseList.size());
        assertEquals(restaurant1.getId(), responseList.get(0).getId());
        assertEquals(restaurant1.getName(), responseList.get(0).getName());
        assertEquals(restaurant1.getCnpj(), responseList.get(0).getCnpj());
        assertEquals(restaurant2.getId(), responseList.get(1).getId());
        assertEquals(restaurant2.getName(), responseList.get(1).getName());
        assertEquals(restaurant2.getCnpj(), responseList.get(1).getCnpj());
    }
}
