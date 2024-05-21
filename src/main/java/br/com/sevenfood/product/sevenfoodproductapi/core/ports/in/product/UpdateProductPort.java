package br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.product;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;

public interface UpdateProductPort {
    Product update(Long id, Product product);
}
