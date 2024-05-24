package br.com.sevenfood.product.sevenfoodproductapi.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Schema(description = "Product", requiredProperties = {"id, code, name, price, productCategory, restaurant"})
@Tag(name = "Product", description = "Model")
public class Product implements Serializable {
    @Schema(description = "Unique identifier of the Product.",
            example = "1")
    private Long id;
    @Schema(description = "code of the Product.",
            example = "V$")
    private String code;
    @Schema(description = "name of the Product.",
            example = "V$")
    private String name;
    @Schema(description = "name of the Product.",
            example = "V$")
    private String description;
    @Schema(description = "name of the Product.",
            example = "V$")
    private BigDecimal price;
    @Schema(description = "name of the Product.",
            example = "V$")
    private String pic;
    @Schema(description = "name of the Product.",
            example = "V$")
    private Long productCategoryId;
    @Schema(description = "name of the Product.",
            example = "V$")
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
