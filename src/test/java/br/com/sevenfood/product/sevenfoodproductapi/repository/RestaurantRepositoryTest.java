package br.com.sevenfood.product.sevenfoodproductapi.repository;

import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@DataJpaTest
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class RestaurantRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private RestaurantEntity restaurantEntity;

    @BeforeEach
    public void setUp() {
        restaurantRepository.deleteAll();

        restaurantEntity = RestaurantEntity.builder()
                .name("Seven Food")
                .cnpj("02.365.347/0001-63")
                .build();
        restaurantEntity = restaurantRepository.save(restaurantEntity);
    }

    @Disabled
    void should_find_no_restaurants_if_repository_is_empty() {
        restaurantRepository.deleteAll();
        Iterable<RestaurantEntity> seeds = restaurantRepository.findAll();
        assertThat(seeds).isEmpty();
    }

    @Disabled
    void should_store_a_restaurant() {
        String nomeFilial = "Seven Food Filial";
        Optional<RestaurantEntity> restaurant = restaurantRepository.findByName(nomeFilial);

        if (!restaurant.isPresent()) {
            RestaurantEntity restaurantFilial = RestaurantEntity.builder()
                    .name(nomeFilial)
                    .cnpj("93.563.658/0001-92")
                    .build();

            RestaurantEntity savedRestaurant = restaurantRepository.save(restaurantFilial);
            Optional<RestaurantEntity> restaurantResponse = restaurantRepository.findByName(nomeFilial);

            RestaurantEntity restaurant1 = restaurantResponse.orElse(null);
            assertThat(restaurant1).isNotNull();
            assertThat(restaurant1).hasFieldOrPropertyWithValue("name", nomeFilial);
        }
    }

    @Disabled
    void whenConstraintViolationExceptionThrown_thenAssertionSucceeds() {
        RestaurantEntity restaurant = createInvalidRestaurant();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            restaurantRepository.save(restaurant);
        });

        String expectedMessage = "tamanho deve ser entre 1 e 255";
        String actualMessage = exception.getMessage();

        log.info("Actual Exception Message: {}", actualMessage);

        assertThat(actualMessage).contains(expectedMessage);
    }

    private RestaurantEntity createInvalidRestaurant() {
        return RestaurantEntity.builder()
                .name("") // Nome vazio pode causar uma violação
                .build();
    }

    @Disabled
    void should_find_null_restaurant() {
        Optional<RestaurantEntity> fromDb = restaurantRepository.findById(99L);
        assertThat(fromDb).isEmpty();
    }

    @Disabled
    void whenFindById_thenReturnRestaurant() {
        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(restaurantEntity.getId());
        assertThat(restaurant).isPresent();
        restaurant.ifPresent(restaurantResult -> assertThat(restaurantResult).hasFieldOrPropertyWithValue("name", "Seven Food"));
    }

    @Disabled
    void whenInvalidId_thenReturnNull() {
        RestaurantEntity fromDb = restaurantRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Disabled
    void givenSetOfRestaurants_whenFindAll_thenReturnAllRestaurants() {
        RestaurantEntity restaurant1 = RestaurantEntity.builder()
                .name("Seven Food - MG")
                .cnpj("12.345.678/0001-90")
                .build();
        RestaurantEntity restaurant2 = RestaurantEntity.builder()
                .name("Seven Food - SC")
                .cnpj("23.456.789/0001-01")
                .build();

        restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2));

        Iterable<RestaurantEntity> allRestaurants = restaurantRepository.findAll();
        List<RestaurantEntity> restaurantList = new ArrayList<>();
        allRestaurants.forEach(restaurantList::add);

        assertThat(restaurantList).hasSize(3).extracting(RestaurantEntity::getName)
                .containsExactlyInAnyOrder("Seven Food", "Seven Food - MG", "Seven Food - SC");
    }
}
