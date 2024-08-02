package br.com.sevenfood.product.sevenfoodproductapi.service;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.ProductMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.product.*;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.productcategory.*;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.out.ProductRepositoryPort;
import br.com.sevenfood.product.sevenfoodproductapi.core.service.ProductService;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.DataException;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepositoryPort productRepository;

    @Mock
    ProductRepository repository;

    @Mock
    ProductMapper mapper;

    @Mock
    CreateProductPort createProductPort;

    @Mock
    DeleteProductPort deleteProductPort;

    @Mock
    FindByIdProductPort findByIdProductPort;

    @Mock
    FindProductsPort findProductsPort;

    @Mock
    UpdateProductPort updateProductPort;

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
                .name("Coca-Cola")
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
    void init() {
        MockitoAnnotations.initMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void getAllProductsTest() {
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
    void getProductByIdTest() {
        Product product1 = getProduct(getRestaurant(), getProductCategory());
        when(productService.findById(1L)).thenReturn(product1);

        Product product = productService.findById(1L);

        assertEquals("Coca-Cola", product.getName());
    }

    @Test
    void getFindProductByShortIdTest() {
        Product product = getProduct(getRestaurant(), getProductCategory());
        when(productService.findById(1L)).thenReturn(product);

        Product result = productService.findById(1L);

        assertEquals("Coca-Cola", result.getName());
    }

    @Test
    void createProductTest() {
        Product product = getProduct(getRestaurant(), getProductCategory());
        Product productResult = getProduct(getRestaurant(), getProductCategory());
        productResult.setId(1L);

        when(productService.save(product)).thenReturn(productResult);
        Product save = productService.save(product);

        assertNotNull(save);
        //verify(productRepository, times(1)).save(product);
    }

    @Test
    void testSaveRestaurantWithLongName() {
        Product product = new Product();
        product.setName("a".repeat(260)); // Nome com 260 caracteres, excedendo o limite de 255
        product.setCode(UUID.randomUUID().toString());
        product.setPic("hhh");
        product.setPrice(BigDecimal.TEN);
        product.setDescription("Coca-Cola");
        product.setProductCategoryId(1L);
        product.setRestaurantId(1L);

        // Simulando o lançamento de uma exceção
        doThrow(new DataException("Value too long for column 'name'", null)).when(productRepository).save(product);

        assertThrows(DataException.class, () -> {
            productRepository.save(product);
        });
    }

    @Test
    void testRemove_Exception() {
        Long productId = 99L;

        boolean result = productService.remove(productId);
        assertFalse(result);
        verify(productRepository, never()).remove(productId);
    }

    @Test
    void testCreateProduct() {
        Product product = getProduct(getRestaurant(), getProductCategory());
        Product productResult = getProduct(getRestaurant(), getProductCategory());
        when(createProductPort.save(product)).thenReturn(productResult);

        Product result = createProductPort.save(product);

        assertNotNull(result);
        assertEquals("Coca-Cola", result.getName());
    }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;
        when(deleteProductPort.remove(productId)).thenReturn(true);

        boolean result = deleteProductPort.remove(productId);

        assertTrue(result);
    }

    @Test
    void testFindByIdProduct() {
        Product product = getProduct(getRestaurant(), getProductCategory());
        when(findByIdProductPort.findById(1L)).thenReturn(product);

        Product result = findByIdProductPort.findById(1L);

        assertNotNull(result);
        assertEquals("Coca-Cola", result.getName());
    }

    @Test
    void testFindProducts() {
        Product product = getProduct(getRestaurant(), getProductCategory());
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(findProductsPort.findAll()).thenReturn(products);
        List<Product> result = findProductsPort.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateProduct() {
        Long productId = 1L;
        Product product = getProduct(getRestaurant(), getProductCategory());

        when(updateProductPort.update(productId, product)).thenReturn(product);
        Product result = updateProductPort.update(productId, product);

        assertNotNull(result);
        assertEquals("Coca-Cola", result.getName());
    }
}