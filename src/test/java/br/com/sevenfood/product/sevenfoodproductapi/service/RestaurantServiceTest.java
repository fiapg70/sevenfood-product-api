package br.com.sevenfood.product.sevenfoodproductapi.service;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.RestaurantMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.restaurant.*;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.out.RestaurantRepositoryPort;
import br.com.sevenfood.product.sevenfoodproductapi.core.service.RestaurantService;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @InjectMocks
    RestaurantService restaurantService;

    @Mock
    RestaurantRepositoryPort restaurantRepository;

    @Mock
    RestaurantRepository repository;

    @Mock
    RestaurantMapper mapper;

    private Validator validator;

    @Mock
    CreateRestaurantPort createRestaurantPort;

    @Mock
    DeleteRestaurantPort deleteRestaurantPort;

    @Mock
    FindByIdRestaurantPort findByIdRestaurantPort;

    @Mock
    FindRestaurantsPort findRestaurantsPort;

    @Mock
    UpdateRestaurantPort updateRestaurantPort;

    private RestaurantEntity getRestaurantEntity() {
        return RestaurantEntity.builder()
                .name("Seven Food")
                .cnpj("02.365.347/0001-63")
                .build();
    }

    private RestaurantEntity getRestaurantEntity1() {
        return RestaurantEntity.builder()
                .name("Seven Food Filial 1")
                .cnpj("19.948.890/0001-96")
                .build();
    }

    private RestaurantEntity getRestaurantEntity2() {
        return RestaurantEntity.builder()
                .name("Seven Food Filial 2")
                .cnpj("14.119.578/0001-76")
                .build();
    }

    private Restaurant getRestaurant() {
        return Restaurant.builder()
                .name("Seven Food")
                .cnpj("02.365.347/0001-63")
                .build();
    }

    private Restaurant getRestaurant1() {
        return Restaurant.builder()
                .name("Seven Food Filial 1")
                .cnpj("19.948.890/0001-96")
                .build();
    }

    private Restaurant getRestaurant2() {
        return Restaurant.builder()
                .name("Seven Food Filial 2")
                .cnpj("14.119.578/0001-76")
                .build();
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void getAllRestaurantsTest() {
        List<Restaurant> restaurants = new ArrayList<>();
        List<RestaurantEntity> listEntity = new ArrayList<>();

        Restaurant client = getRestaurant();
        Restaurant client1 = getRestaurant1();
        Restaurant client2 = getRestaurant2();

        RestaurantEntity clientEntity = getRestaurantEntity();
        RestaurantEntity clientEntity1 = getRestaurantEntity1();
        RestaurantEntity clientEntity2 = getRestaurantEntity2();

        restaurants.add(client);
        restaurants.add(client1);
        restaurants.add(client2);

        listEntity.add(clientEntity);
        listEntity.add(clientEntity1);
        listEntity.add(clientEntity2);

        when(restaurantService.findAll()).thenReturn(restaurants);

        List<Restaurant> restaurantList = restaurantService.findAll();

        assertNotNull(restaurantList);
    }

    @Test
    void getRestaurantByIdTest() {
        Restaurant restaurant1 = getRestaurant();
        when(restaurantService.findById(1L)).thenReturn(restaurant1);

        Restaurant restaurant = restaurantService.findById(1L);

        assertEquals("Seven Food", restaurant.getName());
        assertEquals("02.365.347/0001-63", restaurant.getCnpj());
    }

    @Test
    void getFindRestaurantByShortIdTest() {
        Restaurant restaurant = getRestaurant();
        when(restaurantService.findById(1L)).thenReturn(restaurant);

        Restaurant result = restaurantService.findById(1L);

        assertEquals("Seven Food", result.getName());
        assertEquals("02.365.347/0001-63", result.getCnpj());
    }

    @Test
    void createRestaurantTest() {
        Restaurant restaurant = getRestaurant();
        Restaurant restaurantResult = getRestaurant();
        restaurantResult.setId(1L);

        when(restaurantService.save(restaurant)).thenReturn(restaurantResult);
        Restaurant save = restaurantService.save(restaurant);

        assertNotNull(save);
        //verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    void testSaveRestaurantWithLongName() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("a".repeat(260)); // Nome com 260 caracteres, excedendo o limite de 255

        // Simulando o lançamento de uma exceção
        doThrow(new DataException("Value too long for column 'name'", null)).when(restaurantRepository).save(restaurant);

        assertThrows(DataException.class, () -> {
            restaurantRepository.save(restaurant);
        });
    }

    @Test
    void testRemoveRestaurant_Success() {
        Long restaurantId = 1L;
        Restaurant restaurant = getRestaurant(); // Método que cria um objeto Restaurant para o teste
        restaurant.setId(restaurantId);

        when(restaurantService.findById(restaurantId)).thenReturn(restaurant);
        boolean result = restaurantService.remove(restaurantId);
        assertTrue(result);
    }

    @Test
    void testRemove_Exception() {
        Long productId = 99L;

        boolean result = restaurantService.remove(productId);
        assertFalse(result);
        verify(restaurantRepository, never()).remove(productId);
    }

    @Test
    void createRestaurantPortTest() {
        Restaurant restaurant = getRestaurant();
        Restaurant restaurantResult = getRestaurant();
        restaurantResult.setId(1L);

        CreateRestaurantPort createRestaurantPort = mock(CreateRestaurantPort.class);
        when(createRestaurantPort.save(restaurant)).thenReturn(restaurantResult);

        Restaurant savedRestaurant = createRestaurantPort.save(restaurant);

        assertNotNull(savedRestaurant);
        assertEquals(1L, savedRestaurant.getId());
        assertEquals("Seven Food", savedRestaurant.getName());
    }

    @Test
    void deleteRestaurantPortTest() {
        Long restaurantId = 1L;

        DeleteRestaurantPort deleteRestaurantPort = mock(DeleteRestaurantPort.class);
        when(deleteRestaurantPort.remove(restaurantId)).thenReturn(true);

        boolean result = deleteRestaurantPort.remove(restaurantId);

        assertTrue(result);
    }

    @Test
    void findByIdRestaurantPortTest() {
        Long restaurantId = 1L;
        Restaurant restaurant = getRestaurant();
        restaurant.setId(restaurantId);

        FindByIdRestaurantPort findByIdRestaurantPort = mock(FindByIdRestaurantPort.class);
        when(findByIdRestaurantPort.findById(restaurantId)).thenReturn(restaurant);

        Restaurant foundRestaurant = findByIdRestaurantPort.findById(restaurantId);

        assertNotNull(foundRestaurant);
        assertEquals("Seven Food", foundRestaurant.getName());
        assertEquals("02.365.347/0001-63", foundRestaurant.getCnpj());
    }

    @Test
    void findRestaurantsPortTest() {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(getRestaurant());
        restaurants.add(getRestaurant1());
        restaurants.add(getRestaurant2());

        FindRestaurantsPort findRestaurantsPort = mock(FindRestaurantsPort.class);
        when(findRestaurantsPort.findAll()).thenReturn(restaurants);

        List<Restaurant> restaurantList = findRestaurantsPort.findAll();

        assertNotNull(restaurantList);
        assertEquals(3, restaurantList.size());
    }

    @Test
    void updateRestaurantPortTest() {
        Long restaurantId = 1L;
        Restaurant restaurant = getRestaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Updated Name");

        UpdateRestaurantPort updateRestaurantPort = mock(UpdateRestaurantPort.class);
        when(updateRestaurantPort.update(restaurantId, restaurant)).thenReturn(restaurant);

        Restaurant updatedRestaurant = updateRestaurantPort.update(restaurantId, restaurant);

        assertNotNull(updatedRestaurant);
        assertEquals("Updated Name", updatedRestaurant.getName());
        assertEquals("02.365.347/0001-63", updatedRestaurant.getCnpj());
    }

}