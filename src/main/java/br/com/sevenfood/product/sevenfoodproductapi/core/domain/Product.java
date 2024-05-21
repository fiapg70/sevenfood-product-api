package br.com.sevenfood.product.sevenfoodproductapi.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Tag(name = "Resident object")
public class Product implements Serializable {
    private Long id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private String pic;
    private Long productCategoryId;
    private Long restaurantId;

    public void update(Long id, Product product) {
        this.id = id;
        this.code = product.getCode();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.pic = product.getPic();
        this.productCategoryId = product.getProductCategoryId();
        this.restaurantId = product.getRestaurantId();
    }
}
