package br.com.sevenfood.product.sevenfoodproductapi.application.mapper;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.ProductCategoryMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductCategoryMapperTest {

    private final ProductCategoryMapper mapper = Mappers.getMapper(ProductCategoryMapper.class);

    @Test
    void testFromModelToEntity() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(1L);
        productCategory.setName("Electronics");

        ProductCategoryEntity entity = mapper.fromModelTpEntity(productCategory);

        assertNotNull(entity);
        assertEquals(productCategory.getId(), entity.getId());
        assertEquals(productCategory.getName(), entity.getName());
    }

    @Test
    void testFromEntityToModel() {
        ProductCategoryEntity entity = new ProductCategoryEntity();
        entity.setId(1L);
        entity.setName("Electronics");

        ProductCategory productCategory = mapper.fromEntityToModel(entity);

        assertNotNull(productCategory);
        assertEquals(entity.getId(), productCategory.getId());
        assertEquals(entity.getName(), productCategory.getName());
    }

    @Test
    void testMapList() {
        ProductCategoryEntity entity1 = new ProductCategoryEntity();
        entity1.setId(1L);
        entity1.setName("Electronics");

        ProductCategoryEntity entity2 = new ProductCategoryEntity();
        entity2.setId(2L);
        entity2.setName("Books");

        List<ProductCategoryEntity> entityList = Arrays.asList(entity1, entity2);
        List<ProductCategory> modelList = mapper.map(entityList);

        assertNotNull(modelList);
        assertEquals(2, modelList.size());
        assertEquals(entity1.getId(), modelList.get(0).getId());
        assertEquals(entity1.getName(), modelList.get(0).getName());
        assertEquals(entity2.getId(), modelList.get(1).getId());
        assertEquals(entity2.getName(), modelList.get(1).getName());
    }
}
