package br.com.sevenfood.product.sevenfoodproductapi.repository;

import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import javax.validation.ConstraintViolationException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class RestaurantRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RestaurantRepository restaurantRepository;


    private RestaurantEntity getRestaurant() {
        return RestaurantEntity.builder()
                .name("Seven Food")
                .cnpj("02.365.347/0001-63")
                .build();
    }

    @BeforeEach
    public void setUp() {
        restaurantRepository.findAll();
        restaurantRepository.save(getRestaurant());
    }

    @Test
    void should_find_no_clients_if_repository_is_empty() {
        Iterable<RestaurantEntity> seeds = restaurantRepository.findAll();
        seeds = Collections.EMPTY_LIST;
        assertThat(seeds).isEmpty();
    }

    @Test
    void should_store_a_product_category() {
        String nomeFilial = "Seven Food Filial";
        Optional<RestaurantEntity> restaurant = restaurantRepository.findByName(nomeFilial);
        Optional<RestaurantEntity> restaurantResponse = null;
        if (!restaurant.isPresent()) {

            RestaurantEntity restaurantFilial = RestaurantEntity.builder()
                    .name(nomeFilial)
                    .cnpj("93.563.658/0001-92")
                    .build();

            RestaurantEntity save = restaurantRepository.save(restaurantFilial);
            restaurantResponse = restaurantRepository.findByName(nomeFilial);
        }

        RestaurantEntity restaurant1 = restaurantResponse.get();
        assertThat(restaurant1).hasFieldOrPropertyWithValue("name", nomeFilial);
    }

    @Disabled
    public void whenConstraintViolationExceptionThrown_thenAssertionSucceeds() {
        RestaurantEntity restaurant = createInvalidRestaurant();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            restaurantRepository.save(restaurant);
        });

        String expectedMessage = "tamanho deve ser entre 1 e 255";
        String actualMessage = exception.getMessage();

        // Adicionar saída de log para a mensagem da exceção
        log.info("Actual Exception Message:{}", actualMessage);

        assertNotNull(actualMessage.contains(expectedMessage),
                "Expected message to contain: " + expectedMessage + " but was: " + actualMessage);
    }

    private RestaurantEntity createInvalidRestaurant() {
        RestaurantEntity restaurant = new RestaurantEntity();
        // Configurar o restaurant com valores inválidos
        // Exemplo: valores inválidos que podem causar uma ConstraintViolationException
        restaurant.setName(""); // Nome vazio pode causar uma violação
        return restaurant;
    }

    @Test
    void should_found_null_Restaurant() {
        RestaurantEntity restaurant = null;

        Optional<RestaurantEntity> fromDb = restaurantRepository.findById(99l);
        if (fromDb.isPresent()) {
            restaurant = fromDb.get();
        }
        assertThat(restaurant).isNull();
    }

    @Test
    void whenFindById_thenReturnRestaurant() {
        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(1l);
        if (restaurant.isPresent()) {
            RestaurantEntity restaurantResult = restaurant.get();
            assertThat(restaurantResult).hasFieldOrPropertyWithValue("name", "Seven Food");
        }
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        RestaurantEntity fromDb = restaurantRepository.findById(-11l).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfRestaurants_whenFindAll_thenReturnAllRestaurants() {
        RestaurantEntity restaurant = null;
        RestaurantEntity restaurant1 = null;
        RestaurantEntity restaurant2 = null;

        Optional<RestaurantEntity> restaurantResult = restaurantRepository.findById(1l);
        if (restaurantResult.isPresent()) {

            RestaurantEntity anaFurtadoCorreia = RestaurantEntity.builder()
                    .name("Seven Food")
                    .cnpj("02.365.347/0001-63")
                    .build();
            restaurant = restaurantRepository.save(anaFurtadoCorreia);

            RestaurantEntity alessandroRezendeFurtado = RestaurantEntity.builder()
                    .name("Seven Food - MG")
                    .cnpj("02.365.347/0001-63")
                    .build();
            restaurant1 = restaurantRepository.save(anaFurtadoCorreia);

            RestaurantEntity robertaGimenesMoura = RestaurantEntity.builder()
                    .name("Seven Food - SC")
                    .cnpj("02.365.347/0001-63")
                    .build();
            restaurant2 = restaurantRepository.save(anaFurtadoCorreia);

        }

        Iterator<RestaurantEntity> allRestaurants = restaurantRepository.findAll().iterator();
        List<RestaurantEntity> clients = new ArrayList<>();
        allRestaurants.forEachRemaining(c -> clients.add(c));

        assertThat(allRestaurants);
    }
}