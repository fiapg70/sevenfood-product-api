package br.com.sevenfood.product.sevenfoodproductapi.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Product", requiredProperties = {"id, code, name, price, productCategory, restaurant"})
@Tag(name = "Product", description = "Model")
public class Restaurant implements Serializable {
    @Schema(description = "Unique identifier of the Product.",
            example = "1")
    private Long id;
    @Schema(description = "Unique identifier of the Product.",
            example = "1")
    private String name;
    @Schema(description = "Unique identifier of the Product.",
            example = "1")
    private String cnpj;

    public void update(Long id, Restaurant product) {
        this.id = id;
        this.name = product.getName();
        this.cnpj = product.getCnpj();
    }
}
