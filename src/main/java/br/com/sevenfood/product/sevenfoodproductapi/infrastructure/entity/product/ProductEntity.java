package br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product;

import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.domain.AuditDomain;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.restaurant.RestaurantEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ProductEntity", requiredProperties = {"id, code, name, price, productCategory, restaurant"})
@Tag(name = "ProductEntity", description = "Model")
public class ProductEntity extends AuditDomain {

    @Schema(description = "Unique identifier of the Product.",
            example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Schema(description = "name of the Product.",
            example = "V$")
    @NotNull(message = "o campo \"code\" é obrigario")
    @Size(min = 3, max = 255)
    @Column(name = "code", length = 255)

    private String code;

    @Schema(description = "name of the Product.",
            example = "V$")
    @NotNull(message = "o campo \"name\" é obrigario")
    @Size(min = 1, max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Schema(description = "picture of the Product.",
            example = "V$")
    private String pic;

    @Schema(description = "description of the Product.",
            example = "V$")
    @Size(min = 0, max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Schema(description = "price of the Product.",
            example = "V$")
    @NotNull(message = "o campo \"price\" é obrigario")
    private BigDecimal price;

    @Schema(description = "Restaurant of the User.",
            example = "1", ref = "ProductCategoryEntity")
    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_category_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProductCategoryEntity productCategory;

    @Schema(description = "Restaurant of the User.",
            example = "1", ref = "RestaurantEntity")
    @NotNull
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private RestaurantEntity restaurant;

    public void update(Long id, ProductEntity productEntity) {
        this.id = id;
        this.name = productEntity.getName();
        this.pic = productEntity.getPic();
        this.description = productEntity.getDescription();
        this.price = productEntity.getPrice();
        this.productCategory = productEntity.getProductCategory();
        this.restaurant = productEntity.getRestaurant();
    }
}
