package br.com.sevenfood.product.sevenfoodproductapi.service;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.ProductCategoryMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.out.ProductCategoryRepositoryPort;
import br.com.sevenfood.product.sevenfoodproductapi.core.service.ProductCategoryService;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductCategoryRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceTest {

    @InjectMocks
    ProductCategoryService productCategoryService;

    @Mock
    ProductCategoryRepositoryPort productCategoryRepository;

    @Mock
    ProductCategoryRepository repository;

    @Mock
    ProductCategoryMapper mapper;

    private Validator validator;

    private ProductCategoryEntity getProductCategoryEntity() {
        return ProductCategoryEntity.builder()
                .name("Bebida")
                .build();
    }

    private ProductCategoryEntity getProductCategoryEntity1() {
        return ProductCategoryEntity.builder()
                .name("Bebida 1")
                .build();
    }

    private ProductCategoryEntity getProductCategoryEntity2() {
        return ProductCategoryEntity.builder()
                .name("Bebida 2")
                .build();
    }

    private ProductCategory getProductCategory() {
        return ProductCategory.builder()
                .name("Bebida")
                .build();
    }

    private ProductCategory getProductCategory1() {
        return ProductCategory.builder()
                .name("Bebida 1")
                .build();
    }

    private ProductCategory getProductCategory2() {
        return ProductCategory.builder()
                .name("Bebida 2")
                .build();
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void getAllProductCategorysTest() {
        List<ProductCategory> productCategorys = new ArrayList<>();
        List<ProductCategoryEntity> listEntity = new ArrayList<>();

        ProductCategory client = getProductCategory();
        ProductCategory client1 = getProductCategory1();
        ProductCategory client2 = getProductCategory2();

        ProductCategoryEntity clientEntity = getProductCategoryEntity();
        ProductCategoryEntity clientEntity1 = getProductCategoryEntity1();
        ProductCategoryEntity clientEntity2 = getProductCategoryEntity2();

        productCategorys.add(client);
        productCategorys.add(client1);
        productCategorys.add(client2);

        listEntity.add(clientEntity);
        listEntity.add(clientEntity1);
        listEntity.add(clientEntity2);

        when(productCategoryService.findAll()).thenReturn(productCategorys);

        List<ProductCategory> productCategoryList = productCategoryService.findAll();

        assertNotNull(productCategoryList);
    }

    @Test
    void getProductCategoryByIdTest() {
        ProductCategory productCategory1 = getProductCategory();
        when(productCategoryService.findById(1L)).thenReturn(productCategory1);

        ProductCategory productCategory = productCategoryService.findById(1L);

        assertEquals("Bebida", productCategory.getName());
    }

    @Test
    void getFindProductCategoryByShortIdTest() {
        ProductCategory productCategory = getProductCategory();
        when(productCategoryService.findById(1L)).thenReturn(productCategory);

        ProductCategory result = productCategoryService.findById(1L);

        assertEquals("Bebida", result.getName());
    }

    @Test
    void createProductCategoryTest() {
        ProductCategory productCategory = getProductCategory();
        ProductCategory productCategoryResult = getProductCategory();
        productCategoryResult.setId(1L);

        when(productCategoryService.save(productCategory)).thenReturn(productCategoryResult);
        ProductCategory save = productCategoryService.save(productCategory);

        assertNotNull(save);
        //verify(productCategoryRepository, times(1)).save(productCategory);
    }

    @Test
    void testSaveRestaurantWithLongName() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("a".repeat(260)); // Nome com 260 caracteres, excedendo o limite de 255

        // Simulando o lançamento de uma exceção
        doThrow(new DataException("Value too long for column 'name'", null)).when(productCategoryRepository).save(productCategory);

        assertThrows(DataException.class, () -> {
            productCategoryRepository.save(productCategory);
        });
    }

    @Test
    void testRemoveRestaurant_Success() {
        Long restaurantId = 1L;
        ProductCategory productCategory = getProductCategory();
        productCategory.setId(restaurantId);

        when(productCategoryService.findById(restaurantId)).thenReturn(productCategory);
        boolean result = productCategoryService.remove(restaurantId);
        assertTrue(result);
    }

    @Test
    void testRemove_Exception() {
        Long productId = 99L;

        boolean result = productCategoryService.remove(productId);
        assertFalse(result);
        verify(productCategoryRepository, never()).remove(productId);
    }
}