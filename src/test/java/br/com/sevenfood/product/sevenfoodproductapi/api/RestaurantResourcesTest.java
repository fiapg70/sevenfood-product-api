package br.com.sevenfood.product.sevenfoodproductapi.api;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.ProductRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.RestaurantRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.mappper.RestaurantApiMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.core.service.RestaurantService;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductCategoryRepository;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductRepository;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.RestaurantRepository;
import br.com.sevenfood.product.sevenfoodproductapi.util.CnpjGenerator;
import br.com.sevenfood.product.sevenfoodproductapi.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@TestPropertySource("classpath:application-test.properties")
class RestaurantResourcesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RestaurantService service;

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private RestaurantApiMapper productApiMapper;

    private Restaurant restaurant;

    private Restaurant getRestaurant() {
        return Restaurant.builder()
                .name("Seven Food - Filial")
                .cnpj("02.365.347/0001-63")
                .build();
    }

    private Restaurant getRestaurantUpdate() {
        return Restaurant.builder()
                .id(1l)
                .name("Seven Food - Filial")
                .cnpj("02.365.347/0001-63")
                .build();
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        repository.deleteAll();
        this.restaurant = service.save(getRestaurant());
    }

    @Test
    void findsTaskById() throws Exception {
        mockMvc.perform(get("/v1/restaurants/{id}", this.restaurant.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Seven Food - Filial"));
    }

    @Test
    void getAll() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/restaurants")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").exists());
    }


    @Test
    void create() throws Exception {
        Restaurant restaurantToSave = getRestaurant();
        restaurantToSave.setCnpj(CnpjGenerator.generateCnpj());
        String create = JsonUtil.getJson(restaurantToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/restaurants")
                        .content(create)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    void create_isNull() throws Exception {
        String create = JsonUtil.getJson(new Restaurant());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/restaurants")
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
        RestaurantRequest restaurantRequest = new RestaurantRequest();
        String create = JsonUtil.getJson(restaurantRequest);

        when(productApiMapper.fromRquest(restaurantRequest)).thenThrow(new RuntimeException("Restaurante não encontroado ao atualizar"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/restaurants")
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
        repository.deleteAll();
        Restaurant savedRestaurant = service.save(getRestaurantUpdate());
        Long id = savedRestaurant.getId();
        savedRestaurant.setCnpj(CnpjGenerator.generateCnpj());
        String update = JsonUtil.getJson(savedRestaurant);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/restaurants/{id}", id)
                        .content(update)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Seven Food - Filial"));
    }

    @Test
    void update_isNull() throws Exception {
        String update = JsonUtil.getJson(new Restaurant());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/restaurants/{id}", 99L)
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
        RestaurantRequest product = new RestaurantRequest();
        String create = JsonUtil.getJson(product);

        when(productApiMapper.fromRquest(product)).thenThrow(new RuntimeException("Restaurante não encontroado ao atualizar"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/restaurants/{id}", 99L)
                        .content(create)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }

    @Test
    void deleteRestaurantAPI() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.delete("/v1/restaurants/{id}", 1) )
                .andExpect(status().isNoContent());
    }

    @Test
    void findById_productIsNull() throws Exception {
        MvcResult result = mockMvc.perform(get("/v1/restaurants/{id}", 99L))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }

    @Test
    void testById_Exception() throws Exception {
        RestaurantRequest restaurantRequest = new RestaurantRequest();
        when(productApiMapper.fromRquest(restaurantRequest)).thenThrow(new RuntimeException("Restaurante não encontrado ao buscar por id"));

        MvcResult result = mockMvc.perform(get("/v1/restaurants/{id}", 99L))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }
}