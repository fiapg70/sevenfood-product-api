package br.com.sevenfood.product.sevenfoodproductapi.application.database.repositoryImpl;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.exception.ResourceFoundException;
import br.com.sevenfood.product.sevenfoodproductapi.application.database.repository.RestaurantRepositoryAdapter;
import br.com.sevenfood.product.sevenfoodproductapi.commons.exception.CNPJFoundException;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.factory.ObjectFactory;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.RestaurantMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryAdapterTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantRepositoryAdapter restaurantRepositoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(RestaurantRepositoryAdapterTest.class);
        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        restaurantRepository.save(restaurantEntity);
    }

    @Test
    void testSave() {
        Long id = 1L;
        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        restaurantEntity.setId(id);

        Restaurant restaurant = ObjectFactory.getInstance().getRestaurant();
        restaurant.setId(id);

        when(restaurantMapper.fromModelToEntity(restaurant)).thenReturn(restaurantEntity);
        when(restaurantRepository.findByCnpj(restaurantEntity.getCnpj())).thenReturn(Optional.empty());

        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);
        when(restaurantMapper.fromEntityToModel(restaurantEntity)).thenReturn(restaurant);

        Restaurant savedRestaurant = restaurantRepositoryAdapter.save(restaurant);

        assertNotNull(savedRestaurant);
        //verify(restaurantRepository, times(1)).save(restaurantEntity);
    }

    @Test
    void testSaveThrowsCNPJFoundException() {
        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        Restaurant restaurant = ObjectFactory.getInstance().getRestaurant();
        when(restaurantMapper.fromModelToEntity(restaurant)).thenReturn(restaurantEntity);
        when(restaurantRepository.findByCnpj(restaurantEntity.getCnpj())).thenReturn(Optional.of(restaurantEntity));

        assertThrows(CNPJFoundException.class, () -> {
            restaurantRepositoryAdapter.save(restaurant);
        });

        //verify(restaurantRepository, never()).save(any(RestaurantEntity.class));
    }

    @Test
    void testSaveThrowsResourceFoundException() {
        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        Restaurant restaurant = ObjectFactory.getInstance().getRestaurant();
        when(restaurantMapper.fromModelToEntity(restaurant)).thenReturn(restaurantEntity);
        when(restaurantRepository.findByCnpj(restaurantEntity.getCnpj())).thenReturn(Optional.empty());
        restaurantEntity.setName(null);
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        assertThrows(ResourceFoundException.class, () -> {
            restaurantRepositoryAdapter.save(restaurant);
        });

        verify(restaurantRepository, times(1)).save(restaurantEntity);
    }

    @Test
    void testRemove() {
        doNothing().when(restaurantRepository).deleteById(anyLong());

        boolean result = restaurantRepositoryAdapter.remove(1L);

        assertTrue(result);
        verify(restaurantRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRemoveThrowsException() {
        doThrow(new RuntimeException()).when(restaurantRepository).deleteById(anyLong());

        boolean result = restaurantRepositoryAdapter.remove(1L);

        assertFalse(result);
        verify(restaurantRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindById() {
        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        Restaurant restaurant = ObjectFactory.getInstance().getRestaurant();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantMapper.fromEntityToModel(restaurantEntity)).thenReturn(restaurant);

        Restaurant foundRestaurant = restaurantRepositoryAdapter.findById(1L);

        assertNotNull(foundRestaurant);
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        Restaurant foundRestaurant = restaurantRepositoryAdapter.findById(1L);

        assertNull(foundRestaurant);
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        Restaurant restaurant = ObjectFactory.getInstance().getRestaurant();
        List<RestaurantEntity> restaurantEntities = List.of(restaurantEntity);
        when(restaurantRepository.findAll()).thenReturn(restaurantEntities);
        when(restaurantMapper.map(restaurantEntities)).thenReturn(List.of(restaurant));

        List<Restaurant> restaurants = restaurantRepositoryAdapter.findAll();

        assertNotNull(restaurants);
        assertEquals(1, restaurants.size());
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        Restaurant restaurant = ObjectFactory.getInstance().getRestaurant();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);
        when(restaurantMapper.fromEntityToModel(restaurantEntity)).thenReturn(restaurant);

        Restaurant updatedRestaurant = restaurantRepositoryAdapter.update(1L, restaurant);

        assertNotNull(updatedRestaurant);
        verify(restaurantRepository, times(1)).findById(1L);
        verify(restaurantRepository, times(1)).save(restaurantEntity);
    }

    @Test
    void testUpdateNotFound() {
        Restaurant restaurant = ObjectFactory.getInstance().getRestaurant();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        Restaurant updatedRestaurant = restaurantRepositoryAdapter.update(1L, restaurant);

        assertNull(updatedRestaurant);
        verify(restaurantRepository, times(1)).findById(1L);
    }
}
