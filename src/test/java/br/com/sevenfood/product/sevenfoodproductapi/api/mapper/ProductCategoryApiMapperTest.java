package br.com.sevenfood.product.sevenfoodproductapi.api.mapper;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.ProductCategoryRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.response.ProductCategoryResponse;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.mapper.ProductCategoryApiMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductCategoryApiMapperTest {

    private final ProductCategoryApiMapper mapper = Mappers.getMapper(ProductCategoryApiMapper.class);

    @Test
    void testFromRequest() {
        // Arrange
        ProductCategoryRequest request = new ProductCategoryRequest();
        request.setName("Electronics");

        // Act
        ProductCategory productCategory = mapper.fromRequest(request);

        // Assert
        assertNotNull(productCategory);
        assertEquals("Electronics", productCategory.getName());
    }

    @Test
    void testFromEntity() {
        // Arrange
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(1L);
        productCategory.setName("Electronics");

        // Act
        ProductCategoryResponse response = mapper.fromEntity(productCategory);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Electronics", response.getName());
    }

    @Test
    void testMap() {
        // Arrange
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setId(1L);
        productCategory1.setName("Electronics");

        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setId(2L);
        productCategory2.setName("Furniture");

        List<ProductCategory> productCategories = Arrays.asList(productCategory1, productCategory2);

        // Act
        List<ProductCategoryResponse> responses = mapper.map(productCategories);

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());

        assertEquals(1L, responses.get(0).getId());
        assertEquals("Electronics", responses.get(0).getName());

        assertEquals(2L, responses.get(1).getId());
        assertEquals("Furniture", responses.get(1).getName());
    }
}
