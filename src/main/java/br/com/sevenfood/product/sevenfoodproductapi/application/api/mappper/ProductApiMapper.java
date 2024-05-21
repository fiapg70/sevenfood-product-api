package br.com.sevenfood.product.sevenfoodproductapi.application.api.mappper;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.ProductRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.response.ProductResponse;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductApiMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "pic", target = "pic")
    @Mapping(source = "productCategoryId", target = "productCategoryId")
    @Mapping(source = "restaurantId", target = "restaurantId")
    Product fromRquest(ProductRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    ProductResponse fromEntidy(Product product);

   List<ProductResponse> map(List<Product> products);

}
