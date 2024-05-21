package br.com.sevenfood.product.sevenfoodproductapi.core.ports.out;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import java.util.List;

public interface ProductCategoryRepositoryPort {
    ProductCategory save(ProductCategory productCategory);
    boolean remove(Long id);
    ProductCategory findById(Long id);
    List<ProductCategory> findAll();

    ProductCategory update(Long id, ProductCategory productCategory);
}
