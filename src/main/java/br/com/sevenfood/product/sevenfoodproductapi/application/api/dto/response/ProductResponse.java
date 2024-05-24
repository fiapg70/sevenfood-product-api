package br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ProductRequest", requiredProperties = {"id", "name", "description", "price", "pic", "productCategoryId", "restaurantId"})
@Tag(name = "ProductRequest", description = "Model")
public class ProductResponse implements Serializable {

    @Schema(description = "Unique identifier of the Driver.",
            example = "1")
    private Long id;

    @Schema(description = "Name of the Product.",
            example = "Vicente")
    @Size(min = 3, max = 255)
    private String name;

    @Schema(description = "Name of the Product.",
            example = "Vicente")
    @Size(min = 3, max = 255)
    private String code;

    @Schema(description = "Description of the Product.",
            example = "Vicente")
    @Size(min = 0, max = 255)
    private String description;

    @Schema(description = "value the Product.",
            example = "V$")
    private BigDecimal price;

    @Schema(description = "value the Product.",
            example = "V$")
    private String pic;

    private ProductCategoryResponse productCategory;

    private RestaurantResponse restaurant;

}
