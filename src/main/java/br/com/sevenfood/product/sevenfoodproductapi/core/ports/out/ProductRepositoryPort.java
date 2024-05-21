package br.com.sevenfood.product.sevenfoodproductapi.core.ports.out;

import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;

import java.util.List;

public interface ProductRepositoryPort {
    Product save(Product product);
    boolean remove(Long id);
    Product findById(Long id);
    List<Product> findAll();
    Product update(Long id, Product product);
    Product findByCode(String code);
}
