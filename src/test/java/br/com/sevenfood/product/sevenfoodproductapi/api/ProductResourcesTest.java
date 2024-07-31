package br.com.sevenfood.product.sevenfoodproductapi.api;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.ProductRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.response.ProductResponse;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.mappper.ProductApiMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.service.ProductCategoryService;
import br.com.sevenfood.product.sevenfoodproductapi.core.service.RestaurantService;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductCategoryRepository;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductRepository;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
import br.com.sevenfood.product.sevenfoodproductapi.util.CnpjGenerator;
import br.com.sevenfood.product.sevenfoodproductapi.util.JsonUtil;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.core.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@TestPropertySource("classpath:application-test.properties")
class ProductResourcesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Mock
    private ProductApiMapper productApiMapper;

    private Faker faker = new Faker();
    private Long productCategoryId;
    private Long restaurantId;
    private Long productId;
    private String productCode;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        productCategoryRepository.deleteAll();
        restaurantRepository.deleteAll();

        ProductCategory productCategory = getProductCategory();
        productCategory = productCategoryService.save(productCategory);
        this.productCategoryId = productCategory.getId();

        Restaurant restaurant = getRestaurant();
        restaurant = restaurantService.save(restaurant);
        this.restaurantId = restaurant.getId();

        Product productFounded = getProduct(productCategoryId, restaurantId);
        var productSaved = service.save(productFounded);
        this.productId = productSaved.getId();
        this.productCode = productSaved.getCode();// Save the product ID for use in tests

        verifyDataSaved(productCategory, restaurant, productSaved);
    }

    private void verifyDataSaved(ProductCategory productCategory, Restaurant restaurant, Product product) {
        assertThat(productCategoryRepository.findById(productCategory.getId())).isPresent();
        assertThat(restaurantRepository.findById(restaurant.getId())).isPresent();
        assertThat(repository.findById(product.getId())).isPresent();
    }

    private ProductCategory getProductCategory() {
        return ProductCategory.builder()
                .name(faker.commerce().department())
                .build();
    }

    private Restaurant getRestaurant() {
        return Restaurant.builder()
                .name(faker.company().name())
                .cnpj(CnpjGenerator.generateCnpj())
                .build();
    }

    private Product getProduct(Long productCategoryId, Long restaurantId) {
        return Product.builder()
                .name(faker.commerce().productName())
                .pic(faker.internet().avatar())
                .price(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100)))
                .description(faker.lorem().sentence())
                .productCategoryId(productCategoryId)
                .restaurantId(restaurantId)
                .build();
    }

    private ProductEntity getProductEntity(Long productCategoryId, Long restaurantId) {
        Optional<ProductCategoryEntity> productCategoryById = productCategoryRepository.findById(productCategoryId);
        Optional<RestaurantEntity> restaurantById = restaurantRepository.findById(restaurantId);

        return ProductEntity.builder()
                .name(faker.commerce().productName())
                .pic(faker.internet().avatar())
                .price(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100)))
                .description(faker.lorem().sentence())
                .productCategory(productCategoryById.isPresent() ? productCategoryById.get() : null)
                .restaurant(restaurantById.isPresent() ? restaurantById.get() : null)
                .build();
    }

    private Product getProductUpdate(Long productCategoryId, Long restaurantId) {
        return Product.builder()
                .id(productId) // Ensure we are updating the same product
                .name(faker.commerce().productName())
                .pic(faker.internet().avatar())
                .price(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100)))
                .description(faker.lorem().sentence())
                .productCategoryId(productCategoryId)
                .restaurantId(restaurantId)
                .build();
    }

    @Test
    void findsTaskById() throws Exception {
        MvcResult result = mockMvc.perform(get("/v1/products/{id}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        mockMvc.perform(get("/v1/products/{id}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void getAll() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").exists());
    }

    @Test
    void getAll_isNull() throws Exception {
        repository.deleteAll();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }

    @Test
    void create() throws Exception {
        repository.deleteAll();
        productCategoryRepository.findById(this.productCategoryId).ifPresent(productCategory -> {
            assertThat(productCategory).isNotNull();
            this.productCategoryId = productCategory.getId();
        });

        restaurantRepository.findById(this.restaurantId).ifPresent(restaurant -> {
            assertThat(restaurant).isNotNull();
            this.restaurantId = restaurant.getId();
        });

        Product product = getProduct(this.productCategoryId, this.restaurantId);
        String create = JsonUtil.getJson(product);

        assertThat(create).isNotNull().isNotEmpty();  // Verifique se o JSON não é nulo ou vazio

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/products")
                        .content(create)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/products")
                        .content(create)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        //.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    void create_isNull() throws Exception {
        String create = JsonUtil.getJson(new Product());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/products")
                        .content(create)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }

    @Test
    void testSave_Exception() throws Exception {
        ProductRequest product = new ProductRequest();
        String create = JsonUtil.getJson(product);

        when(productApiMapper.fromRquest(product)).thenThrow(new RuntimeException("Exception message"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/products")
                        .content(create)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }

    @Test
    void update() throws Exception {
        Product productUpdate = getProductUpdate(productCategoryId, restaurantId);
        String update = JsonUtil.getJson(productUpdate);
        System.out.println("Generated JSON for Update: " + update);

        assertThat(update).isNotNull().isNotEmpty();  // Verifique se o JSON não é nulo ou vazio

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/products/{id}", productId)
                        .content(update)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/products/{id}", productId)
                        .content(update)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
    }

    @Test
    void update_isNull() throws Exception {
        String update = JsonUtil.getJson(new Product());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/products/{id}", productId)
                        .content(update)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }

    @Test
    void testUpdate_Exception() throws Exception {
        ProductRequest product = new ProductRequest();
        String create = JsonUtil.getJson(product);

        when(productApiMapper.fromRquest(product)).thenThrow(new RuntimeException("Exception message"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/products/{id}", productId)
                        .content(create)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }

    @Test
    void delete() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/products/{id}", productId))
                .andExpect(status().isNoContent())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/products/{id}", productId))
                .andExpect(status().isNoContent());
    }

    @Test
    void findByCode_productFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/v1/products/code/{code}", this.productCode))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        mockMvc.perform(get("/v1/products/code/{code}", productCode))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void testByCode_Exception() throws Exception {
        ProductRequest product = new ProductRequest();
        when(productApiMapper.fromRquest(product)).thenThrow(new RuntimeException("Exception message"));

        MvcResult result = mockMvc.perform(get("/v1/products/code/{code}", 99L))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }

    @Test
    void findByCode_productIsNull() throws Exception {
        String productCode = UUID.randomUUID().toString();
        MvcResult result = mockMvc.perform(get("/v1/products/code/{code}", productCode))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }

    @Test
    void testById_Exception() throws Exception {
        ProductRequest product = new ProductRequest();
        when(productApiMapper.fromRquest(product)).thenThrow(new RuntimeException("Exception message"));

        MvcResult result = mockMvc.perform(get("/v1/products/{id}", 99L))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }
}