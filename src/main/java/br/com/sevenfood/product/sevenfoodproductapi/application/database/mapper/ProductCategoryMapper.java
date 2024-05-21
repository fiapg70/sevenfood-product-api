package br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    @Mapping(source = "name", target = "name")
    ProductCategoryEntity fromModelTpEntity(ProductCategory productCategory);
    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    ProductCategory fromEntityToModel(ProductCategoryEntity productCategoryEntity);

    List<ProductCategory> map(List<ProductCategoryEntity> productCategoryEntities);
}
