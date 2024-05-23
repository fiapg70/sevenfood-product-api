package br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ProductCategoryRequest", requiredProperties = {"id, name"})
public class ProductCategoryRequest implements Serializable {

    @Schema(description = "Unique identifier of the Driver.",
            example = "1")
    private Long id;

    @Schema(description = "Name of the Product Category.",
            example = "Bebida")
    @Size(min = 3, max = 255)
    private String name;
}
