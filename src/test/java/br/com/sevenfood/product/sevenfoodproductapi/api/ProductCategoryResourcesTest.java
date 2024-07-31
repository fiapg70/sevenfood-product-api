package br.com.sevenfood.product.sevenfoodproductapi.api;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.ProductCategoryRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.mappper.ProductCategoryApiMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.service.ProductCategoryService;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductCategoryRepository;
import br.com.sevenfood.product.sevenfoodproductapi.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
class ProductCategoryResourcesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ProductCategoryService service;

    @Autowired
    private ProductCategoryRepository repository;

    private ProductCategory productCategory;

    private Long productCategoryId;

    @Mock
    private ProductCategoryApiMapper productCategoryApiMapper;

    private ProductCategory getProductCategory() {
        return ProductCategory.builder()
                .name("Bebida")
                .build();
    }

    private ProductCategory getProductCategoryUpdate() {
        return ProductCategory.builder()
                .name("Bebida1")
                .build();
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        this.productCategory = service.save(getProductCategory());
        this.productCategoryId = productCategory.getId();
    }

    @Test
    void findsTaskById() throws Exception {
        Long id = productCategory.getId();
        mockMvc.perform(get("/v1/product-categories/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bebida"));
    }

    @Test
    void getAll() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/product-categories")
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
                        .get("/v1/product-categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }

    @Test
    void create() throws Exception {
        String create = JsonUtil.getJson(getProductCategory());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/product-categories")
                        .content(create)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    @Test
    void create_isNull() throws Exception {
        String create = JsonUtil.getJson(new ProductCategory());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/product-categories")
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
        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
        String create = JsonUtil.getJson(productCategoryRequest);

        when(productCategoryApiMapper.fromRquest(productCategoryRequest)).thenThrow(new RuntimeException("Produto não encontroado ao cadastrar"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/product-categories")
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
        ProductCategory savedProductCategory = service.save(getProductCategory());
        Long id = savedProductCategory.getId();
        String update = JsonUtil.getJson(getProductCategoryUpdate());

        mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/product-categories/{id}", id)
                        .content(update)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Bebida1"));
    }

    @Test
    void update_isNull() throws Exception {
        String update = JsonUtil.getJson(new ProductCategoryRequest());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/product-categories/{id}", productCategoryId)
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
        ProductCategoryRequest product = new ProductCategoryRequest();
        String create = JsonUtil.getJson(product);

        when(productCategoryApiMapper.fromRquest(product)).thenThrow(new RuntimeException("Produto não encontroado ao atualizar"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/product-categories/{id}", productCategoryId)
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
        mockMvc.perform( MockMvcRequestBuilders.delete("/v1/product-categories/{id}", 1) )
                .andExpect(status().isNoContent());
    }

    @Test
    void findByCode_productIsNull() throws Exception {
        MvcResult result = mockMvc.perform(get("/v1/product-categories/{id}", 99l))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }

    @Test
    void testById_Exception() throws Exception {
        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
        when(productCategoryApiMapper.fromRquest(productCategoryRequest)).thenThrow(new RuntimeException("Produto não encontrado ao buscar por código"));

        MvcResult result = mockMvc.perform(get("/v1/product-categories/{id}", 99L))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEmpty();
    }
}