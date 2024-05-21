package br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.productcategory;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;

public interface UpdateProductCategoryPort {
    ProductCategory update(Long id, ProductCategory productCategory);
}
