package br.com.sevenfood.product.sevenfoodproductapi.application.api.mappper;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.RestaurantRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.response.RestaurantResponse;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantApiMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "cnpj", target = "cnpj")
    Restaurant fromRquest(RestaurantRequest request);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    RestaurantResponse fromEntidy(Restaurant restaurant);

   List<RestaurantResponse> map(List<Restaurant> restaurants);
}
