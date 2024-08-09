package br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
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
@Schema(description = "ProductRequest", requiredProperties = {"id", "name", "description", "price", "pic", "productCategoryId", "restaurantId"})
@Tag(name = "ProductRequest", description = "Model")
public class ProductRequest implements Serializable {

    @Schema(description = "Unique identifier of the Driver.",
            example = "1")
    private Long id;

    @Schema(description = "Name of the Product.",
            example = "Coca-cola")
    @Size(min = 3, max = 255)
    private String name;

    @Schema(description = "Description of the Product.",
            example = "Coca-cola !L")
    @Size(min = 0, max = 255)
    private String description;

    @Schema(description = "Price of the Product.",
            example = "9.00")
    private BigDecimal price;

    @Schema(description = "Picture of the Product.",
            example = "/home/pic/bebida.png")
    private String pic;

    @Schema(description = "Product Category of the Product.",
            example = "Bebida", ref = "ProductCategory")
    private Long productCategoryId;

    @Schema(description = "Restaurant of the Product.",
            example = "seven food", ref = "Restaurant")
    private Long restaurantId;
}
