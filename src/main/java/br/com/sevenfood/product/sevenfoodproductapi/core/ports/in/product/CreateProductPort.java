package br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.product;


import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;

public interface CreateProductPort {
    Product save(Product product);
}
