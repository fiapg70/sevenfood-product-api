package br.com.sevenfood.product.sevenfoodproductapi.api.mapper;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.ProductRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.response.ProductResponse;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.mappper.ProductApiMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductApiMapperTest {

    private ProductApiMapper productApiMapper;

    @BeforeEach
    public void setUp() {
        productApiMapper = Mappers.getMapper(ProductApiMapper.class);
    }

    @Test
    void testFromRequest() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setName("Product Name");
        request.setDescription("Product Description");
        request.setPrice(BigDecimal.valueOf(10.0));
        request.setPic("pic.jpg");
        request.setProductCategoryId(1L);
        request.setRestaurantId(2L);

        // Act
        Product product = productApiMapper.fromRquest(request);

        // Assert
        assertNotNull(product);
        assertEquals(request.getName(), product.getName());
        assertEquals(request.getDescription(), product.getDescription());
        assertEquals(request.getPrice(), product.getPrice());
        assertEquals(request.getPic(), product.getPic());
        assertEquals(request.getProductCategoryId(), product.getProductCategoryId());
        assertEquals(request.getRestaurantId(), product.getRestaurantId());
    }

    @Test
    void testFromEntity() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setCode("P001");
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(BigDecimal.valueOf(10.0));
        product.setPic("pic.jpg");
        product.setProductCategoryId(1L);
        product.setRestaurantId(2L);

        // Act
        ProductResponse response = productApiMapper.fromEntidy(product);

        // Assert
        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getCode(), response.getCode());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getPrice(), response.getPrice());
        assertEquals(product.getPic(), response.getPic());
        //assertEquals(product.getProductCategoryId(), response.getProductCategory().getId());
        //assertEquals(product.getRestaurantId(), response.getProductCategory().getId());
    }

    @Test
    void testMap() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setCode("P001");
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(BigDecimal.valueOf(10.0));
        product1.setPic("pic1.jpg");
        product1.setProductCategoryId(1L);
        product1.setRestaurantId(2L);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setCode("P002");
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(BigDecimal.valueOf(20.0));
        product2.setPic("pic2.jpg");
        product2.setProductCategoryId(3L);
        product2.setRestaurantId(4L);

        List<Product> products = Arrays.asList(product1, product2);

        // Act
        List<ProductResponse> responses = productApiMapper.map(products);

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());

        ProductResponse response1 = responses.get(0);
        assertEquals(product1.getId(), response1.getId());
        assertEquals(product1.getCode(), response1.getCode());
        assertEquals(product1.getName(), response1.getName());
        assertEquals(product1.getDescription(), response1.getDescription());
        assertEquals(product1.getPrice(), response1.getPrice());
        assertEquals(product1.getPic(), response1.getPic());
        //assertEquals(product1.getProductCategoryId(), response1.getProductCategory().getId());
        //assertEquals(product1.getRestaurantId(), response1.getRestaurant().getId());

        ProductResponse response2 = responses.get(1);
        assertEquals(product2.getId(), response2.getId());
        assertEquals(product2.getCode(), response2.getCode());
        assertEquals(product2.getName(), response2.getName());
        assertEquals(product2.getDescription(), response2.getDescription());
        assertEquals(product2.getPrice(), response2.getPrice());
        assertEquals(product2.getPic(), response2.getPic());
       // assertEquals(product2.getProductCategoryId(), response2.getProductCategory().getId());
       // assertEquals(product2.getRestaurantId(), response2.getRestaurant().getId());
    }
}
