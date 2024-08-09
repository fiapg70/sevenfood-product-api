package br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "RestaurantResponse", requiredProperties = {"id, code, cnpj"})
@Tag(name = "RestaurantResponse", description = "Model")
public class RestaurantResponse implements Serializable {

    @Schema(description = "Unique identifier of the Driver.",
            example = "1")
    private Long id;

    @Schema(description = "Name of the Product.",
            example = "Vicente")
    @Size(min = 3, max = 255)
    private String name;

    @Schema(description = "Description of the Product.",
            example = "Vicente")
    @Size(min = 0, max = 255)
    private String cnpj;

}
