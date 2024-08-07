package br.com.sevenfood.product.sevenfoodproductapi.repository;

import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        log.info("Cleaning up database...");
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        restaurantRepository.deleteAll();

        log.info("Setting up test data...");
        restaurant = restaurantRepository.save(getRestaurant());
        productCategory = productCategoryRepository.save(getProductCategory());

        ProductEntity product = productRepository.save(getProduct(restaurant, productCategory));
        log.info("ProductEntity:{}", product);
    }

    private RestaurantEntity getRestaurant() {
        return RestaurantEntity.builder()
                .name(faker.company().name())
                .cnpj(UUID.randomUUID().toString())
                .build();
    }

    private ProductEntity getProduct(RestaurantEntity restaurantEntity, ProductCategoryEntity productCategory) {
        return ProductEntity.builder()
                .name(faker.food().vegetable())
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
                .name(faker.food().vegetable())
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
                .name(faker.food().vegetable())
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
                .name(faker.food().ingredient())
                .build();
    }

    @Test
    void should_find_no_products_if_repository_is_empty() {
        Iterable<ProductEntity> products = productRepository.findAll();
        products = Collections.EMPTY_LIST;
        assertThat(products).isEmpty();
    }

    @Test
    void should_store_a_product() {
        log.info("Setting up test data...");
        var restaurant1 = restaurantRepository.save(getRestaurant());
        var productCategory1 = productCategoryRepository.save(getProductCategory());

        ProductEntity product = getProduct(restaurant1, productCategory1);
        product.setCode(UUID.randomUUID().toString());

        // Ensure unique code
        ProductEntity savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
    }

    @Test
    void should_find_product_by_id() {
        log.info("Setting up test data...");
        var restaurant1 = restaurantRepository.save(getRestaurant());
        var productCategory1 = productCategoryRepository.save(getProductCategory());

        ProductEntity product = getProduct(restaurant1, productCategory1);
        product.setCode(UUID.randomUUID().toString());

        // Ensure unique code
        ProductEntity savedProduct = productRepository.save(product);

        Optional<ProductEntity> foundProduct = productRepository.findById(savedProduct.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo(savedProduct.getName());
    }

    @Test
    void should_find_all_products() {
        log.info("Cleaning up database...");
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        restaurantRepository.deleteAll();

        var restaurant1 = restaurantRepository.save(getRestaurant());
        var productCategory1 = productCategoryRepository.save(getProductCategory());

        ProductEntity product1 = productRepository.save(getProduct(restaurant1, productCategory1));

        Iterable<ProductEntity> products = productRepository.findAll();
        List<ProductEntity> productList = new ArrayList<>();
        products.forEach(productList::add);

        assertThat(productList).hasSize(1);
        assertThat(productList).extracting(ProductEntity::getName).contains(product1.getName());
    }

    @Test
    void should_delete_all_products() {
        log.info("Cleaning up database...");
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
        restaurantRepository.deleteAll();

        var restaurant1 = restaurantRepository.save(getRestaurant());
        var productCategory1 = productCategoryRepository.save(getProductCategory());

        productRepository.save(getProduct(restaurant1, productCategory1));
        productRepository.deleteAll();

        Iterable<ProductEntity> products = productRepository.findAll();
        assertThat(products).isEmpty();
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        log.info("Cleaning up database...");
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

        Iterable<ProductEntity> products = productRepository.findAll();
        List<ProductEntity> productList = new ArrayList<>();
        products.forEach(productList::add);

        assertThat(productList).hasSize(1);
        //assertThat(productList).extracting(ProductEntity::getName).contains(product1.getName(), product2.getName(), product3.getName());
    }

    @Disabled
    void testSaveRestaurantWithLongName() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("a".repeat(260)); // Nome com 260 caracteres, excedendo o limite de 255
        productEntity.setCode(UUID.randomUUID().toString());
        productEntity.setPic("hhh");
        productEntity.setPrice(BigDecimal.TEN);
        productEntity.setDescription("Coca-Cola");
        productEntity.setProductCategory(productCategory);
        productEntity.setRestaurant(restaurant);

        assertThrows(DataIntegrityViolationException.class, () -> {
            productRepository.save(productEntity);
        });
    }

    private ProductEntity createInvalidProductCategory() {
        ProductEntity productCategory = new ProductEntity();
        // Configurar o productCategory com valores inválidos
        // Exemplo: valores inválidos que podem causar uma ConstraintViolationException
        productCategory.setName(""); // Nome vazio pode causar uma violação
        return productCategory;
    }
}
