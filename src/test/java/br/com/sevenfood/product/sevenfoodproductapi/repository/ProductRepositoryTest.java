package br.com.sevenfood.product.sevenfoodproductapi.repository;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductCategoryRepository;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductRepository;
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
import org.springframework.test.context.TestPropertySource;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private RestaurantEntity restaurant;
    private ProductCategoryEntity productCategory;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        restaurantRepository.deleteAll();

        restaurant = restaurantRepository.save(getRestaurant());
        productCategory = productCategoryRepository.save(getProductCategory());

        ProductEntity product = productRepository.save(getProduct(restaurant, productCategory));
        log.info("ProductEntity:{}", product);
    }

    private RestaurantEntity getRestaurant() {
        return RestaurantEntity.builder()
                .name("Seven Food")
                .cnpj(UUID.randomUUID().toString())
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

    private ProductEntity getProduct1(RestaurantEntity restaurantEntity, ProductCategoryEntity productCategory) {
        return ProductEntity.builder()
                .name("Bebida2")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategory(productCategory)
                .restaurant(restaurantEntity)
                .build();
    }

    private ProductEntity getProduct2(RestaurantEntity restaurantEntity, ProductCategoryEntity productCategory) {
        return ProductEntity.builder()
                .name("Bebida3")
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

    @Test
    void should_find_no_products_if_repository_is_empty() {
        productRepository.deleteAll();
        Iterable<ProductEntity> products = productRepository.findAll();
        assertThat(products).isEmpty();
    }

    @Test
    void should_store_a_product() {
        ProductEntity product = getProduct(restaurant, productCategory);
        ProductEntity savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
    }

    @Test
    void should_find_product_by_id() {
        ProductEntity product = getProduct(restaurant, productCategory);
        ProductEntity savedProduct = productRepository.save(product);

        Optional<ProductEntity> foundProduct = productRepository.findById(savedProduct.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo(savedProduct.getName());
    }

    @Test
    void should_find_all_products() {
        ProductEntity product1 = productRepository.save(getProduct(restaurant, productCategory));
        ProductEntity product2 = productRepository.save(getProduct(restaurant, productCategory));
        ProductEntity product3 = productRepository.save(getProduct(restaurant, productCategory));

        Iterable<ProductEntity> products = productRepository.findAll();
        List<ProductEntity> productList = new ArrayList<>();
        products.forEach(productList::add);

        assertThat(productList).hasSize(3);
        assertThat(productList).extracting(ProductEntity::getName).contains(product1.getName(), product2.getName(), product3.getName());
    }

    @Test
    void should_delete_all_products() {
        ProductEntity product = productRepository.save(getProduct(restaurant, productCategory));
        productRepository.deleteAll();

        Iterable<ProductEntity> products = productRepository.findAll();
        assertThat(products).isEmpty();
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        ProductEntity fromDb = productRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfProducts_whenFindAll_thenReturnAllProducts() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        restaurantRepository.deleteAll();

        List<ProductEntity> all = productRepository.findAll();
        log.info(all.toString());

        RestaurantEntity restaurant1 = restaurantRepository.save(getRestaurant());
        ProductCategoryEntity productCategory1 = productCategoryRepository.save(getProductCategory());

        ProductEntity product = getProduct(restaurant1, productCategory1);
        log.info("ProductEntity:{}", product);
        ProductEntity product1 = productRepository.save(product);

        ProductEntity product2 = getProduct1(restaurant1, productCategory1);
        log.info("ProductEntity:{}", product2);
        ProductEntity product3 = productRepository.save(product2);

        Iterable<ProductEntity> products = productRepository.findAll();
        List<ProductEntity> productList = new ArrayList<>();
        products.forEach(productList::add);

        assertThat(productList).hasSize(2);
        //assertThat(productList).extracting(ProductEntity::getName).contains(product1.getName(), product2.getName(), product3.getName());
    }

    @Disabled
    void whenConstraintViolationExceptionThrown_thenAssertionSucceeds() {
        ProductEntity productCategory = createInvalidProductCategory();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            productRepository.save(productCategory);
        });

        String expectedMessage = "tamanho deve ser entre 1 e 255";
        String actualMessage = exception.getMessage();

        // Adicionar saída de log para a mensagem da exceção
        log.info("Actual Exception Message:{}", actualMessage);

        assertTrue(actualMessage.contains(expectedMessage),
                "Expected message to contain: " + expectedMessage + " but was: " + actualMessage);
    }

    private ProductEntity createInvalidProductCategory() {
        ProductEntity productCategory = new ProductEntity();
        // Configurar o productCategory com valores inválidos
        // Exemplo: valores inválidos que podem causar uma ConstraintViolationException
        productCategory.setName(""); // Nome vazio pode causar uma violação
        return productCategory;
    }
}
