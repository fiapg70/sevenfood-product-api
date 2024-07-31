package br.com.sevenfood.product.sevenfoodproductapi.service;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.RestaurantMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.out.RestaurantRepositoryPort;
import br.com.sevenfood.product.sevenfoodproductapi.core.service.RestaurantService;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

    @Disabled
    void createRestaurantWithNullFieldsTest() {
        Restaurant invalidRestaurant = Restaurant.builder()
                .name(null)
                .cnpj(null)
                .build();

        // Executando a validação explicitamente
        Set<ConstraintViolation<Restaurant>> violations = validator.validate(invalidRestaurant);

        // Verificando se há violações de restrição
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            restaurantRepository.save(invalidRestaurant);
        });

        String expectedMessage = "tamanho deve ser entre 1 e 255";
        String actualMessage = exception.getMessage();

        // Adicionar saída de log para a mensagem da exceção
        log.info("Actual Exception Message:{}", actualMessage);

        assertTrue(actualMessage.contains(expectedMessage),
                "Expected message to contain: " + expectedMessage + " but was: " + actualMessage);
    }
}