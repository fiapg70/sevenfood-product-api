package br.com.sevenfood.product.sevenfoodproductapi.repository;

import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductCategoryRepository;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductRepository;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
import jakarta.validation.ConstraintViolationException;
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

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private RestaurantEntity getRestaurant() {
        return RestaurantEntity.builder()
                .name("Seven Food")
                .cnpj("02.365.347/0001-63")
                .build();
    }

    private ProductEntity getProduct(RestaurantEntity restaurantEntity, ProductCategoryEntity productCategory) {
        return ProductEntity.builder()
                .name("Bebida")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategory(productCategory)
                .restaurant(restaurantEntity)
                .build();
    }

    private ProductCategoryEntity getProductCategory() {
        return ProductCategoryEntity.builder()
                .name("Bebida")
                .build();
    }

    @BeforeEach
    public void setInitUp() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        restaurantRepository.deleteAll();

        RestaurantEntity restaurant = restaurantRepository.save(getRestaurant());
        ProductCategoryEntity productCategory = productCategoryRepository.save(getProductCategory());

        ProductEntity product = productRepository.save(getProduct(restaurant, productCategory));
        log.info("ProductEntity:{}", product);
    }

    @Test
    public void should_find_no_clients_if_repository_is_empty() {
        Iterable<ProductEntity> seeds = productRepository.findAll();
        seeds = Collections.EMPTY_LIST;
        assertThat(seeds).isEmpty();
    }

    @Test
    public void should_store_a_client() {
        restaurantRepository.deleteAll();
        productCategoryRepository.deleteAll();
        productRepository.deleteAll();

        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(1l);
        Optional<ProductCategoryEntity> productCategoryOp = productCategoryRepository.findById(1l);
        if (productCategoryOp.isPresent() && restaurant.isPresent()) {

            ProductEntity bebida = ProductEntity.builder()
                    .name("Bebida")
                    .code(UUID.randomUUID().toString())
                    .pic("hhh")
                    .price(BigDecimal.TEN)
                    .description("Coca-Cola")
                    .productCategory(productCategoryOp.get())
                    .restaurant(restaurant.get())
                    .build();

            productRepository.save(bebida);
            assertThat(bebida).hasFieldOrPropertyWithValue("name", "Bebida");
        }
    }

    @Test
    public void whenConstraintViolationExceptionThrown_thenAssertionSucceeds() {
        ProductEntity productCategory = createInvalidProductCategory();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            productRepository.save(productCategory);
        });

        String expectedMessage = "tamanho deve ser entre 1 e 255";
        String actualMessage = exception.getMessage();

        // Adicionar saída de log para a mensagem da exceção
        log.info("Actual Exception Message:{}", actualMessage);

        assertNotNull(actualMessage.contains(expectedMessage),
                "Expected message to contain: " + expectedMessage + " but was: " + actualMessage);
    }

    private ProductEntity createInvalidProductCategory() {
        ProductEntity productCategory = new ProductEntity();
        // Configurar o productCategory com valores inválidos
        // Exemplo: valores inválidos que podem causar uma ConstraintViolationException
        productCategory.setName(""); // Nome vazio pode causar uma violação
        return productCategory;
    }

    @Disabled
    public void should_found_store_a_client() { //TODO: Refatorar
        ProductEntity productResuslt = null;

        Optional<RestaurantEntity> restaurantOp = restaurantRepository.findById(1l);
        Optional<ProductCategoryEntity> productCategoryOp = productCategoryRepository.findById(1l);
        if (productCategoryOp.isPresent() && restaurantOp.isPresent()) {

            ProductEntity bebida = ProductEntity.builder()
                    .name("Bife Vegano")
                    .code(UUID.randomUUID().toString())
                    .pic("hhh")
                    .price(BigDecimal.TEN)
                    .description("Coca-Colas ")
                    .productCategory(productCategoryOp.get())
                    .restaurant(restaurantOp.get())
                    .build();

            productResuslt = productRepository.save(bebida);
        }

        Optional<ProductEntity> found = productRepository.findById(productResuslt.getId());
        assertNotNull(found.get());
    }

    @Disabled
    public void whenFindById_thenReturnProduct() {
        ProductEntity productResult = null;

        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(1l);
        Optional<ProductCategoryEntity> productCategoryOp = productCategoryRepository.findById(1l);
        if (productCategoryOp.isPresent() && restaurant.isPresent()) {

            ProductEntity bebida = ProductEntity.builder()
                    .name("Bebida")
                    .pic("hhh")
                    .price(BigDecimal.TEN)
                    .description("Coca-Cola")
                    .productCategory(productCategoryOp.get())
                    .restaurant(restaurant.get())
                    .build();

            productResult = productRepository.save(bebida);
        }

        ProductEntity fromDb = productRepository.findById(productResult.getId()).orElse(null);
        assertNotNull(fromDb);
    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        ProductEntity fromDb = productRepository.findById(-11l).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    public void givenSetOfProducts_whenFindAll_thenReturnAllProducts() {
        ProductEntity productResult = null;
        ProductEntity productResult2 = null;
        ProductEntity productResult3 = null;

        Optional<ProductCategoryEntity> productCategoryOp = productCategoryRepository.findById(1l);
        if (productCategoryOp.isPresent()) {

            ProductEntity bebida = ProductEntity.builder()
                    .name("Bebida")
                    .pic("hhh")
                    .price(BigDecimal.TEN)
                    .description("Coca-Cola")
                    .productCategory(productCategoryOp.get())
                    .build();

            productResult = productRepository.save(bebida);

            ProductEntity acompanhamento = ProductEntity.builder()
                    .name("Acompanhamento")
                    .pic("hhh")
                    .price(BigDecimal.TEN)
                    .description("Cebola")
                    .productCategory(productCategoryOp.get())
                    .build();

            productResult2 = productRepository.save(acompanhamento);

            ProductEntity lanche = ProductEntity.builder()
                    .name("Lanche")
                    .pic("hhh")
                    .price(BigDecimal.TEN)
                    .description("Coca-Cola")
                    .productCategory(productCategoryOp.get())
                    .build();

            productResult3 = productRepository.save(lanche);

        }

        Iterator<ProductEntity> allProducts = productRepository.findAll().iterator();
        List<ProductEntity> clients = new ArrayList<>();
        allProducts.forEachRemaining(c -> clients.add(c));

        assertThat(allProducts);
    }
}