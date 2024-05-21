package br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.product;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;

public interface FindByIdProductPort {
    Product findById(Long id);
    Product findByCode(String code);
}
