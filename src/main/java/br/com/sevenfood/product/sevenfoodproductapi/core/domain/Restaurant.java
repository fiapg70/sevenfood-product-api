package br.com.sevenfood.product.sevenfoodproductapi.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ProductCategoryRequest", requiredProperties = {"id, name"})
public class Restaurant implements Serializable {
    private Long id;
    private String name;
    private String cnpj;

    public void update(Long id, Restaurant product) {
        this.id = id;
        this.name = product.getName();
        this.cnpj = product.getCnpj();
    }
}
