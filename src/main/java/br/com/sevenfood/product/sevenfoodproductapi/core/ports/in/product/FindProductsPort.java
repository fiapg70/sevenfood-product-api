package br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.product;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;

import java.util.List;

public interface FindProductsPort {
    List<Product> findAll();
}
