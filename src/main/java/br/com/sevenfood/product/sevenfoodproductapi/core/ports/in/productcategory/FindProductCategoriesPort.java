package br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.productcategory;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;

import java.util.List;

public interface FindProductCategoriesPort {
    List<ProductCategory> findAll();
}
