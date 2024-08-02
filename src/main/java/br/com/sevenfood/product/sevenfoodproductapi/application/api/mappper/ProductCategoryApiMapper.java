package br.com.sevenfood.product.sevenfoodproductapi.application.api.mappper;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.ProductCategoryRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.response.ProductCategoryResponse;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductCategoryApiMapper {

    @Mapping(source = "name", target = "name")
    ProductCategory fromRquest(ProductCategoryRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    ProductCategoryResponse fromEntidy(ProductCategory productCategory);

   List<ProductCategoryResponse> map(List<ProductCategory> productCategories);
}
