package br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "pic", target = "pic")
    @Mapping(source = "productCategoryId", target = "productCategory.id")
    @Mapping(source = "restaurantId", target = "restaurant.id")
    ProductEntity fromModelTpEntity(Product product);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "productCategoryId", source = "productCategory.id")
    @Mapping(target = "restaurantId", source = "restaurant.id")
    Product fromEntityToModel(ProductEntity productEntity);

    List<Product> map(List<ProductEntity> productEntities);
}
