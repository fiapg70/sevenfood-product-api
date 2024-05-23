package br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.domain.AuditDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_product_category")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ProductCategoryRequest", requiredProperties = {"id, name"})
public class ProductCategoryEntity extends AuditDomain {

    @Schema(description = "Unique identifier of the Product.",
            example = "1", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Schema(description = "name of the Product.",
            example = "V$", required = true)
    @NotNull(message = "o campo \"name\" é obrigario")
    @Size(min = 1, max = 255)
    @Column(name = "name", length = 255)
    private String name;

    public void update(Long id, ProductCategory productCategory) {
        this.id = id;
        this.name = productCategory.getName();
    }
}
