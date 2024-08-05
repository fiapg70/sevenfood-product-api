package br.com.sevenfood.product.sevenfoodproductapi.repository.impl;

import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByCode() {
        // Arrange
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setCode("ABC123");
        product.setName("Test Product");

        when(productRepository.findByCode(anyString())).thenReturn(product);

        // Act
        ProductEntity foundProduct = productRepository.findByCode("ABC123");

        // Assert
        assertNotNull(foundProduct);
        assertEquals("ABC123", foundProduct.getCode());
        assertEquals("Test Product", foundProduct.getName());
    }

    @Test
    void testFindByCode_NotFound() {
        // Arrange
        when(productRepository.findByCode(anyString())).thenReturn(null);

        // Act
        ProductEntity foundProduct = productRepository.findByCode("NON_EXISTENT_CODE");

        // Assert
        assertNull(foundProduct);
    }
}
