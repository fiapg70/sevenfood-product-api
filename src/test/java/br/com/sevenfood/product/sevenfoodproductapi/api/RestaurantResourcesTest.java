package br.com.sevenfood.product.sevenfoodproductapi.api;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.core.service.RestaurantService;
import br.com.sevenfood.product.sevenfoodproductapi.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@TestPropertySource("classpath:application-test.properties")
public class RestaurantResourcesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RestaurantService service;

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

    @Test
    void findsTaskById() throws Exception {
        Long id = 1l;

        mockMvc.perform(get("/v1/restaurants/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Seven Food - Filial"));
    }

    @Test
    public void getAll() throws Exception
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
    public void create() throws Exception {
        String create = JsonUtil.getJson(getRestaurant());

        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/restaurants")
                        .content(create)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }


    @Test
    public void update() throws Exception {
        String update = JsonUtil.getJson(getRestaurantUpdate());

        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/restaurants/{id}", 1)
                        .content(update)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Seven Food - Filial"));
    }

    @Test
    public void deleteEmployeeAPI() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders.delete("/v1/restaurants/{id}", 1) )
                .andExpect(status().isOk());
    }
}