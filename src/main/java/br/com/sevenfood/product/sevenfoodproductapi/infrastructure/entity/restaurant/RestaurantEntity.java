package br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.domain.AuditDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_restaurant")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "RestaurantEntity", requiredProperties = {"id, code, cnpj"})
@Tag(name = "RestaurantEntity", description = "Model")
public class RestaurantEntity extends AuditDomain {

    @Schema(description = "Unique identifier of the Product.",
            example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Schema(description = "name of the Product.",
            example = "V$")
    @NotNull(message = "o campo \"name\" é obrigario")
    @Size(min = 1, max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Schema(description = "cnpj of the Product.",
            example = "V$")
    @NotNull(message = "o campo \"cnpj\" é obrigatorio")
    @Size(min = 3, max = 255)
    @Column(name = "cnpj", length = 255, unique = true)
    @CNPJ(message = "CNPJ inválido")
    private String cnpj;

    public void update(Long id, RestaurantEntity restaurantEntity) {
        this.id = id;
        this.name = restaurantEntity.getName();
        this.cnpj = restaurantEntity.getCnpj();
    }
}
