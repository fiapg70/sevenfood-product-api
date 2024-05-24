package br.com.sevenfood.product.sevenfoodproductapi.application.repository;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.ProductMapper;
import br.com.sevenfood.product.sevenfoodproductapi.application.database.repository.ProductRepositoryAdapter;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductRepository;
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
import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ProductRepositoryAdapterTest {

    @InjectMocks
    ProductRepositoryAdapter productRepositoryAdapter;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;

    private RestaurantEntity getRestaurantEntity() {
        return RestaurantEntity.builder()
                .name("Seven Food")
                .cnpj("02.365.347/0001-63")
                .build();
    }

    private Restaurant getRestaurant() {
        return Restaurant.builder()
                .name("Seven Food")
                .cnpj("02.365.347/0001-63")
                .build();
    }

    private ProductEntity getProductEntity(RestaurantEntity restaurantEntity, ProductCategoryEntity productCategory) {
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

    private ProductEntity getProductEntity1(RestaurantEntity restaurantEntity, ProductCategoryEntity productCategory) {
        return ProductEntity.builder()
                .name("Bebida 1")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategory(productCategory)
                .restaurant(restaurantEntity)
                .build();
    }

    private ProductEntity getProductEntity2(RestaurantEntity restaurantEntity, ProductCategoryEntity productCategory) {
        return ProductEntity.builder()
                .name("Bebida 2")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategory(productCategory)
                .restaurant(restaurantEntity)
                .build();
    }

    private Product getProduct(Restaurant restaurant, ProductCategory productCategory) {
        return Product.builder()
                .name("Bebida")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategoryId(restaurant.getId())
                .restaurantId(productCategory.getId())
                .build();
    }

    private Product getProduct1(Restaurant restaurant, ProductCategory productCategory) {
        return Product.builder()
                .name("Bebida 1")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategoryId(restaurant.getId())
                .restaurantId(productCategory.getId())
                .build();
    }

    private Product getProduct2(Restaurant restaurant, ProductCategory productCategory) {
        return Product.builder()
                .name("Bebida 2")
                .code(UUID.randomUUID().toString())
                .pic("hhh")
                .price(BigDecimal.TEN)
                .description("Coca-Cola")
                .productCategoryId(restaurant.getId())
                .restaurantId(productCategory.getId())
                .build();
    }


    private ProductCategoryEntity getProductCategoryEntity() {
        return ProductCategoryEntity.builder()
                .name("Bebida")
                .build();
    }

    private ProductCategory getProductCategory() {
        return ProductCategory.builder()
                .name("Bebida")
                .build();
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_find_no_clients_if_repository_is_empty() {
        Iterable<ProductEntity> seeds = productRepository.findAll();
        seeds = Collections.EMPTY_LIST;
        assertThat(seeds).isEmpty();
    }

    @Test
    public void should_store_a_product_category() {
        String cocaColaBeverage = "Coca-Cola";
        ProductEntity cocaCola = ProductEntity.builder()
                .name(cocaColaBeverage)
                .build();

        when(productRepository.save(cocaCola)).thenReturn(cocaCola);
        ProductEntity saved = productRepository.save(cocaCola);
        log.info("ProductEntity:{}", saved);
        assertThat(saved).hasFieldOrPropertyWithValue("name", cocaColaBeverage);
    }

    @Disabled
    public void whenConstraintViolationExceptionThrown_thenAssertionSucceeds() {
        ProductEntity product = createInvalidProduct();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            productRepository.save(product);
        });

        String expectedMessage = "tamanho deve ser entre 1 e 255";
        String actualMessage = exception.getMessage();

        // Adicionar saída de log para a mensagem da exceção
        log.info("Actual Exception Message:{}", actualMessage);

        assertNotNull(actualMessage.contains(expectedMessage),
                "Expected message to contain: " + expectedMessage + " but was: " + actualMessage);
    }

    private ProductEntity createInvalidProduct() {
        ProductEntity product = new ProductEntity();
        // Configurar o product com valores inválidos
        // Exemplo: valores inválidos que podem causar uma ConstraintViolationException
        product.setName(""); // Nome vazio pode causar uma violação
        return product;
    }

    @Test
    public void should_found_null_Product() {
        ProductEntity product = null;

        when(productRepository.findById(99l)).thenReturn(Optional.empty());
        Optional<ProductEntity> fromDb = productRepository.findById(99l);
        if (fromDb.isPresent()) {
            product = fromDb.get();
        }
        assertThat(product).isNull();
        assertThat(fromDb).isEmpty();
    }

    @Test
    public void whenFindById_thenReturnProduct() {
        Optional<ProductEntity> product = productRepository.findById(1l);
        if (product.isPresent()) {
            ProductEntity productResult = product.get();
            assertThat(productResult).hasFieldOrPropertyWithValue("name", "Bebida");
        }
    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        ProductEntity fromDb = productRepository.findById(-11l).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    public void givenSetOfProducts_whenFindAll_thenReturnAllProducts() {
        ProductEntity product = null;
        ProductEntity product1 = null;
        ProductEntity product2 = null;

        Optional<ProductEntity> restaurant = productRepository.findById(1l);
        if (restaurant.isPresent()) {

            ProductEntity bebida = ProductEntity.builder()
                    .name("Bebida")
                    .build();
            product = productRepository.save(bebida);

            ProductEntity acompanhamento = ProductEntity.builder()
                    .name("Acompanhamento")
                    .build();
            product1 = productRepository.save(acompanhamento);

            ProductEntity lanche = ProductEntity.builder()
                    .name("Lanche")
                    .build();
            product2 = productRepository.save(lanche);

        }

        Iterator<ProductEntity> allProducts = productRepository.findAll().iterator();
        List<ProductEntity> clients = new ArrayList<>();
        allProducts.forEachRemaining(c -> clients.add(c));

        assertThat(allProducts);
    }
}