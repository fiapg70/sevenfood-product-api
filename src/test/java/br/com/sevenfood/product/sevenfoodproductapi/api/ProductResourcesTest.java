package br.com.sevenfood.product.sevenfoodproductapi.api;

import br.com.sevenfood.product.sevenfoodproductapi.core.service.ProductCategoryService;
import br.com.sevenfood.product.sevenfoodproductapi.core.service.RestaurantService;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductCategoryRepository;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductRepository;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
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

    private Faker faker = new Faker();
    private Long productCategoryId;
    private Long restaurantId;
    private Long productId;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        productCategoryRepository.deleteAll();
        restaurantRepository.deleteAll();

        ProductCategory productCategory = getProductCategory();
        productCategory = productCategoryService.save(productCategory);
        productCategoryId = productCategory.getId();

        Restaurant restaurant = getRestaurant();
        restaurant = restaurantService.save(restaurant);
        restaurantId = restaurant.getId();

        Product product = getProduct(productCategoryId, restaurantId);
        product = service.save(product);
        productId = product.getId(); // Save the product ID for use in tests

        verifyDataSaved(productCategory, restaurant, product);
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
                .cnpj(generateUniqueCNPJ())
                .build();
    }

    private String generateUniqueCNPJ() {
        return String.format("%02d.%03d.%03d/%04d-%02d",
                faker.number().numberBetween(10, 99),
                faker.number().numberBetween(100, 999),
                faker.number().numberBetween(100, 999),
                faker.number().numberBetween(1000, 9999),
                faker.number().numberBetween(10, 99));
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

    @Disabled
    void create() throws Exception {
        Product product = getProduct(productCategoryId, restaurantId);
        String create = JsonUtil.getJson(product);
        System.out.println("Generated JSON for Create: " + create);

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
    void delete() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/products/{id}", productId))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/products/{id}", productId))
                .andExpect(status().isOk());
    }
}