package br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.productcategory;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;

public interface CreateProductCategoryPort {
    ProductCategory save(ProductCategory productCategory);
}
