package br.com.sevenfood.product.sevenfoodproductapi.application.database.repositoryImpl;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.ProductCategoryMapper;
import br.com.sevenfood.product.sevenfoodproductapi.application.database.repository.ProductCategoryRepositoryAdapter;
import br.com.sevenfood.product.sevenfoodproductapi.commons.exception.ResourceNotRemoveException;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.factory.ObjectFactory;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProductCategoryRepositoryAdapterTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductCategoryMapper productCategoryMapper;

    @InjectMocks
    private ProductCategoryRepositoryAdapter productCategoryRepositoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(ProductCategoryRepositoryAdapterTest.class);
        ProductCategoryEntity productCategoryEntity = ObjectFactory.getInstance().getProductCategoryEntity();
        productCategoryRepository.save(productCategoryEntity);
    }

    @Test
    void testSave() {
        ProductCategory productCategory = ObjectFactory.getInstance().getProductCategory();
        ProductCategoryEntity productCategoryEntity = ObjectFactory.getInstance().getProductCategoryEntity();
        when(productCategoryMapper.fromModelTpEntity(productCategory)).thenReturn(productCategoryEntity);
        when(productCategoryRepository.save(productCategoryEntity)).thenReturn(productCategoryEntity);
        when(productCategoryMapper.fromEntityToModel(productCategoryEntity)).thenReturn(productCategory);

        ProductCategory savedProductCategory = productCategoryRepositoryAdapter.save(productCategory);

        assertNotNull(savedProductCategory);
    }

    @Test
    void testSave_isNull() {
        ProductCategory savedProductCategory = productCategoryRepositoryAdapter.save(new ProductCategory());

        assertNull(savedProductCategory);
    }

    @Test
    void testRemove() {
        doNothing().when(productCategoryRepository).deleteById(1L);

        boolean result = productCategoryRepositoryAdapter.remove(1L);

        assertTrue(result);
        verify(productCategoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindById() {
        ProductCategory productCategory = ObjectFactory.getInstance().getProductCategory();
        ProductCategoryEntity productCategoryEntity = ObjectFactory.getInstance().getProductCategoryEntity();
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(productCategoryEntity));
        when(productCategoryMapper.fromEntityToModel(productCategoryEntity)).thenReturn(productCategory);

        ProductCategory found = productCategoryRepositoryAdapter.findById(1L);

        assertNotNull(found);
        verify(productCategoryRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdate() {
        ProductCategory productCategory = ObjectFactory.getInstance().getProductCategory();
        ProductCategoryEntity productCategoryEntity = ObjectFactory.getInstance().getProductCategoryEntity();
        when(productCategoryRepository.findById(1L)).thenReturn(Optional.of(productCategoryEntity));
        when(productCategoryRepository.save(productCategoryEntity)).thenReturn(productCategoryEntity);
        when(productCategoryMapper.fromEntityToModel(productCategoryEntity)).thenReturn(productCategory);

        ProductCategory updatedProductCategory = productCategoryRepositoryAdapter.update(1L, productCategory);

        assertNotNull(updatedProductCategory);
        verify(productCategoryRepository, times(1)).findById(1L);
        verify(productCategoryRepository, times(1)).save(productCategoryEntity);
    }

    @Test
    void testUpdate_isNull() {
        ProductCategory updatedProductCategory = productCategoryRepositoryAdapter.update(1L, new ProductCategory());
        assertNull(updatedProductCategory);
    }

    @Test
    void testRemoveThrowsException() {
        doThrow(new ResourceNotRemoveException("Erro ao deletar")).when(productCategoryRepository).deleteById(anyLong());
        boolean result = productCategoryRepositoryAdapter.remove(99L);
        assertFalse(result, "O método remove deve retornar false em caso de exceção");
    }
}