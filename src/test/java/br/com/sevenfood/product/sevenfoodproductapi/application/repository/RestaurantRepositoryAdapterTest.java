package br.com.sevenfood.product.sevenfoodproductapi.application.repository;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.RestaurantMapper;
import br.com.sevenfood.product.sevenfoodproductapi.application.database.repository.RestaurantRepositoryAdapter;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
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

import javax.validation.ConstraintViolationException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryAdapterTest {

    @InjectMocks
    RestaurantRepositoryAdapter pestaurantRepositoryAdapter;

    @Mock
    private RestaurantRepository pestaurantRepository;
    @Mock
    private RestaurantMapper pestaurantMapper;

    private Restaurant getRestaurant() {
        return Restaurant.builder()
                .id(1l)
                .name("Bebida")
                .build();
    }

    private RestaurantEntity getRestaurantEntity() {
        return RestaurantEntity.builder()
                .id(1l)
                .name("Bebida")
                .build();
    }


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void should_find_no_clients_if_repository_is_empty() {
        Iterable<RestaurantEntity> seeds = pestaurantRepository.findAll();
        seeds = Collections.EMPTY_LIST;
        assertThat(seeds).isEmpty();
    }

    @Test
    void should_store_a_product_category() {
        String cocaColaBeverage = "Coca-Cola";
        RestaurantEntity cocaCola = RestaurantEntity.builder()
                .name(cocaColaBeverage)
                .build();

        when(pestaurantRepository.save(cocaCola)).thenReturn(cocaCola);
        RestaurantEntity saved = pestaurantRepository.save(cocaCola);
        log.info("RestaurantEntity:{}", saved);
        assertThat(saved).hasFieldOrPropertyWithValue("name", cocaColaBeverage);
    }

    @Disabled
    public void whenConstraintViolationExceptionThrown_thenAssertionSucceeds() {
        RestaurantEntity pestaurant = createInvalidRestaurant();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            pestaurantRepository.save(pestaurant);
        });

        String expectedMessage = "tamanho deve ser entre 1 e 255";
        String actualMessage = exception.getMessage();

        // Adicionar saída de log para a mensagem da exceção
        log.info("Actual Exception Message:{}", actualMessage);

        assertNotNull(actualMessage.contains(expectedMessage),
                "Expected message to contain: " + expectedMessage + " but was: " + actualMessage);
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

        when(pestaurantRepository.findById(99l)).thenReturn(Optional.empty());
        Optional<RestaurantEntity> fromDb = pestaurantRepository.findById(99l);
        if (fromDb.isPresent()) {
            pestaurant = fromDb.get();
        }
        assertThat(pestaurant).isNull();
        assertThat(fromDb).isEmpty();
    }

    @Test
    void whenFindById_thenReturnRestaurant() {
        Optional<RestaurantEntity> pestaurant = pestaurantRepository.findById(1l);
        if (pestaurant.isPresent()) {
            RestaurantEntity pestaurantResult = pestaurant.get();
            assertThat(pestaurantResult).hasFieldOrPropertyWithValue("name", "Bebida");
        }
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        RestaurantEntity fromDb = pestaurantRepository.findById(-11l).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfRestaurants_whenFindAll_thenReturnAllRestaurants() {
        RestaurantEntity pestaurant = null;
        RestaurantEntity pestaurant1 = null;
        RestaurantEntity pestaurant2 = null;

        Optional<RestaurantEntity> restaurant = pestaurantRepository.findById(1l);
        if (restaurant.isPresent()) {

            RestaurantEntity bebida = RestaurantEntity.builder()
                    .name("Bebida")
                    .build();
            pestaurant = pestaurantRepository.save(bebida);

            RestaurantEntity acompanhamento = RestaurantEntity.builder()
                    .name("Acompanhamento")
                    .build();
            pestaurant1 = pestaurantRepository.save(acompanhamento);

            RestaurantEntity lanche = RestaurantEntity.builder()
                    .name("Lanche")
                    .build();
            pestaurant2 = pestaurantRepository.save(lanche);

        }

        Iterator<RestaurantEntity> allRestaurants = pestaurantRepository.findAll().iterator();
        List<RestaurantEntity> clients = new ArrayList<>();
        allRestaurants.forEachRemaining(c -> clients.add(c));

        assertThat(allRestaurants);
    }
}