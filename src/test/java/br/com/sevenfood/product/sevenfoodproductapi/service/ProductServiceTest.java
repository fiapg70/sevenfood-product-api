package br.com.sevenfood.product.sevenfoodproductapi.service;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.ProductMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.out.ProductRepositoryPort;
import br.com.sevenfood.product.sevenfoodproductapi.core.service.ProductService;
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

import javax.validation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepositoryPort productRepository;

    @Mock
    ProductRepository repository;

    @Mock
    ProductMapper mapper;

    private Validator validator;

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
    public void init() {
        MockitoAnnotations.initMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void getAllProductsTest() {
        List<Product> products = new ArrayList<>();
        List<ProductEntity> listEntity = new ArrayList<>();

        Product client = getProduct(getRestaurant(), getProductCategory());
        Product client1 = getProduct1(getRestaurant(), getProductCategory());
        Product client2 = getProduct2(getRestaurant(), getProductCategory());

        ProductEntity clientEntity = getProductEntity(getRestaurantEntity(), getProductCategoryEntity());
        ProductEntity clientEntity1 = getProductEntity1(getRestaurantEntity(), getProductCategoryEntity());
        ProductEntity clientEntity2 = getProductEntity2(getRestaurantEntity(), getProductCategoryEntity());

        products.add(client);
        products.add(client1);
        products.add(client2);

        listEntity.add(clientEntity);
        listEntity.add(clientEntity1);
        listEntity.add(clientEntity2);

        when(productService.findAll()).thenReturn(products);

        List<Product> productList = productService.findAll();

        assertNotNull(productList);
    }

    @Test
    public void getProductByIdTest() {
        Product product1 = getProduct(getRestaurant(), getProductCategory());
        when(productService.findById(1L)).thenReturn(product1);

        Product product = productService.findById(1L);

        assertEquals("Bebida", product.getName());
    }

    @Test
    public void getFindProductByShortIdTest() {
        Product product = getProduct(getRestaurant(), getProductCategory());
        when(productService.findById(1L)).thenReturn(product);

        Product result = productService.findById(1L);

        assertEquals("Bebida", result.getName());
    }

    @Test
    public void createProductTest() {
        Product product = getProduct(getRestaurant(), getProductCategory());
        Product productResult = getProduct(getRestaurant(), getProductCategory());
        productResult.setId(1L);

        when(productService.save(product)).thenReturn(productResult);
        Product save = productService.save(product);

        assertNotNull(save);
        //verify(productRepository, times(1)).save(product);
    }

    @Disabled
    public void createProductWithNullFieldsTest() {
        Product invalidProduct = Product.builder()
                .name(null)
                .build();

        // Executando a validação explicitamente
        Set<ConstraintViolation<Product>> violations = validator.validate(invalidProduct);

        // Verificando se há violações de restrição
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            productRepository.save(invalidProduct);
        });

        String expectedMessage = "tamanho deve ser entre 1 e 255";
        String actualMessage = exception.getMessage();

        // Adicionar saída de log para a mensagem da exceção
        log.info("Actual Exception Message:{}", actualMessage);

        assertTrue(actualMessage.contains(expectedMessage),
                "Expected message to contain: " + expectedMessage + " but was: " + actualMessage);
    }
}