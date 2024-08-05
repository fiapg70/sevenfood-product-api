package br.com.sevenfood.product.sevenfoodproductapi.application.database.repositoryImpl;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.ProductMapper;
import br.com.sevenfood.product.sevenfoodproductapi.application.database.repository.ProductRepositoryAdapter;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.factory.ObjectFactory;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProductRepositoryAdapterTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductRepositoryAdapter productRepositoryAdapter;

    private Product product;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(ProductRepositoryAdapterTest.class);

        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        ProductCategoryEntity productCategoryEntity = ObjectFactory.getInstance().getProductCategoryEntity();
        ProductEntity productEntity = ObjectFactory.getInstance().getProductEntity(restaurantEntity, productCategoryEntity);
        productRepository.save(productEntity);
    }

    @Test
    void testSave() {
        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        ProductCategoryEntity productCategoryEntity = ObjectFactory.getInstance().getProductCategoryEntity();

        ProductEntity productEntity = ObjectFactory.getInstance().getProductEntity(restaurantEntity, productCategoryEntity);
        Restaurant restaurant = ObjectFactory.getInstance().getRestaurant();

        ProductCategory productCategory = ObjectFactory.getInstance().getProductCategory();
        Product product = ObjectFactory.getInstance().getProduct(restaurant, productCategory);

        when(productMapper.fromModelTpEntity(product)).thenReturn(productEntity);
        productEntity.setId(1l);
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(productMapper.fromEntityToModel(productEntity)).thenReturn(product);

        Product savedProduct = productRepositoryAdapter.save(product);

        assertNotNull(savedProduct);
        verify(productRepository, times(1)).save(productEntity);
    }

    @Test
    void testSaveThrowsException() {
        Product savedProduct = productRepositoryAdapter.save(product);
        assertNull(savedProduct);
    }

    @Test
    void testRemove() {
        doNothing().when(productRepository).deleteById(anyLong());

        boolean result = productRepositoryAdapter.remove(1L);

        assertTrue(result);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRemoveThrowsException() {
        doThrow(new RuntimeException()).when(productRepository).deleteById(anyLong());

        boolean result = productRepositoryAdapter.remove(1L);

        assertFalse(result);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindById() {
        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        ProductCategoryEntity productCategoryEntity = ObjectFactory.getInstance().getProductCategoryEntity();

        ProductEntity productEntity = ObjectFactory.getInstance().getProductEntity(restaurantEntity, productCategoryEntity);
        Restaurant restaurant = ObjectFactory.getInstance().getRestaurant();

        ProductCategory productCategory = ObjectFactory.getInstance().getProductCategory();
        Product product = ObjectFactory.getInstance().getProduct(restaurant, productCategory);

        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
        when(productMapper.fromEntityToModel(productEntity)).thenReturn(product);

        Product foundProduct = productRepositoryAdapter.findById(1L);

        assertNotNull(foundProduct);
        //verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Product foundProduct = productRepositoryAdapter.findById(1L);

        assertNull(foundProduct);
        //verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        ProductCategoryEntity productCategoryEntity = ObjectFactory.getInstance().getProductCategoryEntity();

        ProductEntity productEntity = ObjectFactory.getInstance().getProductEntity(restaurantEntity, productCategoryEntity);
        Restaurant restaurant = ObjectFactory.getInstance().getRestaurant();

        ProductCategory productCategory = ObjectFactory.getInstance().getProductCategory();
        Product product = ObjectFactory.getInstance().getProduct(restaurant, productCategory);

        List<ProductEntity> productEntities = List.of(productEntity);
        when(productRepository.findAll()).thenReturn(productEntities);
        when(productMapper.map(productEntities)).thenReturn(List.of(product));

        List<Product> products = productRepositoryAdapter.findAll();

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        ProductCategoryEntity productCategoryEntity = ObjectFactory.getInstance().getProductCategoryEntity();

        ProductEntity productEntity = ObjectFactory.getInstance().getProductEntity(restaurantEntity, productCategoryEntity);
        Restaurant restaurant = ObjectFactory.getInstance().getRestaurant();

        ProductCategory productCategory = ObjectFactory.getInstance().getProductCategory();
        Product product = ObjectFactory.getInstance().getProduct(restaurant, productCategory);

        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        when(productMapper.fromEntityToModel(productEntity)).thenReturn(product);

        Product updatedProduct = productRepositoryAdapter.update(1L, product);

        assertNotNull(updatedProduct);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(productEntity);
    }

    @Test
    void testUpdateNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Product updatedProduct = productRepositoryAdapter.update(1L, product);

        assertNull(updatedProduct);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByCode() {
        RestaurantEntity restaurantEntity = ObjectFactory.getInstance().getRestaurantEntity();
        ProductCategoryEntity productCategoryEntity = ObjectFactory.getInstance().getProductCategoryEntity();

        ProductEntity productEntity = ObjectFactory.getInstance().getProductEntity(restaurantEntity, productCategoryEntity);
        Restaurant restaurant = ObjectFactory.getInstance().getRestaurant();

        ProductCategory productCategory = ObjectFactory.getInstance().getProductCategory();
        Product product = ObjectFactory.getInstance().getProduct(restaurant, productCategory);

        when(productRepository.findByCode("code123")).thenReturn(productEntity);
        when(productMapper.fromEntityToModel(productEntity)).thenReturn(product);

        Product foundProduct = productRepositoryAdapter.findByCode("code123");

        assertNotNull(foundProduct);
        verify(productRepository, times(1)).findByCode("code123");
    }
}
