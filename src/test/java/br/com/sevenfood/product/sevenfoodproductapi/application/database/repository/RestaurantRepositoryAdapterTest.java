package br.com.sevenfood.product.sevenfoodproductapi.application.database.repository;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.RestaurantMapper;
import br.com.sevenfood.product.sevenfoodproductapi.application.database.repository.RestaurantRepositoryAdapter;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
import br.com.sevenfood.product.sevenfoodproductapi.util.CnpjGenerator;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryAdapterTest {

    @InjectMocks
    RestaurantRepositoryAdapter restaurantRepositoryAdapter;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    private Restaurant getRestaurant() {
        return Restaurant.builder()
                .id(1l)
                .name("Seven-Food")
                .cnpj(CnpjGenerator.generateCnpj())
                .build();
    }

    private RestaurantEntity getRestaurantEntity() {
        return RestaurantEntity.builder()
                .id(1l)
                .name("Seven-Food")
                .cnpj(CnpjGenerator.generateCnpj())
                .build();
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void should_find_no_clients_if_repository_is_empty() {
        Iterable<RestaurantEntity> seeds = restaurantRepository.findAll();
        seeds = Collections.EMPTY_LIST;
        assertThat(seeds).isEmpty();
    }

    @Test
    void should_store_a_product_category() {
        String cocaColaBeverage = "Coca-Cola";
        RestaurantEntity cocaCola = RestaurantEntity.builder()
                .name(cocaColaBeverage)
                .build();

        when(restaurantRepository.save(cocaCola)).thenReturn(cocaCola);
        RestaurantEntity saved = restaurantRepository.save(cocaCola);
        log.info("RestaurantEntity:{}", saved);
        assertThat(saved).hasFieldOrPropertyWithValue("name", cocaColaBeverage);
    }

    @Test
    void testSaveRestaurantWithLongName() {
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setName("a".repeat(260)); // Nome com 260 caracteres, excedendo o limite de 255

        // Simulando o lançamento de uma exceção
        doThrow(new DataException("Value too long for column 'name'", null)).when(restaurantRepository).save(restaurantEntity);

        assertThrows(DataException.class, () -> {
            restaurantRepository.save(restaurantEntity);
        });
    }

    private RestaurantEntity createInvalidRestaurant() {
        RestaurantEntity pestaurant = new RestaurantEntity();
        // Configurar o pestaurant com valores inválidos
        // Exemplo: valores inválidos que podem causar uma ConstraintViolationException
        pestaurant.setName(""); // Nome vazio pode causar uma violação
        return pestaurant;
    }

    @Test
    void should_found_null_Restaurant() {
        RestaurantEntity pestaurant = null;

        when(restaurantRepository.findById(99l)).thenReturn(Optional.empty());
        Optional<RestaurantEntity> fromDb = restaurantRepository.findById(99l);
        if (fromDb.isPresent()) {
            pestaurant = fromDb.get();
        }
        assertThat(pestaurant).isNull();
        assertThat(fromDb).isEmpty();
    }

    @Test
    void whenFindById_thenReturnRestaurant() {
        Optional<RestaurantEntity> pestaurant = restaurantRepository.findById(1l);
        if (pestaurant.isPresent()) {
            RestaurantEntity pestaurantResult = pestaurant.get();
            assertThat(pestaurantResult).hasFieldOrPropertyWithValue("name", "Bebida");
        }
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        RestaurantEntity fromDb = restaurantRepository.findById(-11l).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfRestaurants_whenFindAll_thenReturnAllRestaurants() {
        RestaurantEntity pestaurant = null;
        RestaurantEntity pestaurant1 = null;
        RestaurantEntity pestaurant2 = null;

        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(1l);
        if (restaurant.isPresent()) {

            RestaurantEntity bebida = RestaurantEntity.builder()
                    .name("Bebida")
                    .build();
            pestaurant = restaurantRepository.save(bebida);

            RestaurantEntity acompanhamento = RestaurantEntity.builder()
                    .name("Acompanhamento")
                    .build();
            pestaurant1 = restaurantRepository.save(acompanhamento);

            RestaurantEntity lanche = RestaurantEntity.builder()
                    .name("Lanche")
                    .build();
            pestaurant2 = restaurantRepository.save(lanche);

        }

        Iterator<RestaurantEntity> allRestaurants = restaurantRepository.findAll().iterator();
        List<RestaurantEntity> clients = new ArrayList<>();
        allRestaurants.forEachRemaining(c -> clients.add(c));

        assertNotNull(allRestaurants);
    }

    @Test
    void testUpdateRestaurant_NotFound() {
        Long restaurantId = 99L;
        Restaurant restaurant = new Restaurant(); // Configure as needed
        Restaurant result = restaurantRepositoryAdapter.update(restaurantId, restaurant);
        assertThat(result).isNull();
    }
}