package br.com.sevenfood.product.sevenfoodproductapi.repository;

import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductCategoryRepository;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductRepository;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
import com.github.javafaker.Faker;
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

    private RestaurantEntity restaurantEntity;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ProductRepository productRepository;

    Faker faker = new Faker();

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        restaurantRepository.deleteAll();
        productCategoryRepository.deleteAll();

        restaurantEntity = RestaurantEntity.builder()
                .name(faker.company().name())
                .cnpj(generateUniqueCNPJ())
                .build();
        restaurantEntity = restaurantRepository.save(restaurantEntity);
    }

    @Test
    void should_find_no_restaurants_if_repository_is_empty() { //TODO - Refatorar
        productRepository.deleteAll();
        restaurantRepository.deleteAll();
        productCategoryRepository.deleteAll();

        restaurantEntity = RestaurantEntity.builder()
                .name(faker.company().name())
                .cnpj(generateUniqueCNPJ())
                .build();
        restaurantEntity = restaurantRepository.save(restaurantEntity);

        restaurantRepository.deleteAll();
        Iterable<RestaurantEntity> seeds = restaurantRepository.findAll();
        assertThat(seeds).isEmpty();
    }

    private String generateUniqueCNPJ() {
        return String.format("%02d.%03d.%03d/%04d-%02d",
                faker.number().numberBetween(10, 99),
                faker.number().numberBetween(100, 999),
                faker.number().numberBetween(100, 999),
                faker.number().numberBetween(1000, 9999),
                faker.number().numberBetween(10, 99));
    }

    @Test
    void should_store_a_restaurant() {
        String nomeFilial = faker.company().name();
        Optional<RestaurantEntity> restaurant = restaurantRepository.findByName(nomeFilial);

        if (!restaurant.isPresent()) {
            RestaurantEntity restaurant2 = RestaurantEntity.builder()
                    .name(nomeFilial)
                    .cnpj(generateUniqueCNPJ())
                    .build();

            RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant2);
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

    @Test
    void should_find_null_restaurant() {
        Optional<RestaurantEntity> fromDb = restaurantRepository.findById(99L);
        assertThat(fromDb).isEmpty();
    }

    @Test
    void whenFindById_thenReturnRestaurant() {

        productRepository.deleteAll();
        restaurantRepository.deleteAll();
        productCategoryRepository.deleteAll();

        String nameCompany = faker.company().name();
        restaurantEntity = RestaurantEntity.builder()
                .name(nameCompany)
                .cnpj(generateUniqueCNPJ())
                .build();

        restaurantEntity = restaurantRepository.save(restaurantEntity);

        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(restaurantEntity.getId());
        assertThat(restaurant).isPresent();
        restaurant.ifPresent(restaurantResult -> assertThat(restaurantResult).hasFieldOrPropertyWithValue("name", nameCompany));
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        RestaurantEntity fromDb = restaurantRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfRestaurants_whenFindAll_thenReturnAllRestaurants() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        restaurantRepository.deleteAll();

        String nameCompany1 = faker.company().name();
        String nameCompany2 = faker.company().name();

        String cnpj1 = generateUniqueCNPJ();
        String cnpj2 = generateUniqueCNPJ();

        RestaurantEntity restaurant1 = RestaurantEntity.builder()
                .name(nameCompany1)
                .cnpj(cnpj1)
                .build();

        RestaurantEntity restaurant2 = RestaurantEntity.builder()
                .name(nameCompany2)
                .cnpj(cnpj2)
                .build();

        restaurantRepository.saveAll(Arrays.asList(restaurant1, restaurant2));

        Iterable<RestaurantEntity> allRestaurants = restaurantRepository.findAll();
        List<RestaurantEntity> restaurantList = new ArrayList<>();
        allRestaurants.forEach(restaurantList::add);

        assertThat(restaurantList).hasSize(2).extracting(RestaurantEntity::getName)
                .containsExactlyInAnyOrder(nameCompany1, nameCompany2);
    }
}
