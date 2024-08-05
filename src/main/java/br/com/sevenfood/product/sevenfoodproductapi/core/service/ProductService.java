package br.com.sevenfood.product.sevenfoodproductapi.core.service;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.exception.ResourceFoundException;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.product.*;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.out.ProductRepositoryPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService implements CreateProductPort, UpdateProductPort, FindByIdProductPort, FindProductsPort, DeleteProductPort {

    private final ProductRepositoryPort productRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product resultById = findById(id);
        if (resultById != null) {
            resultById.update(id, product);

            return productRepository.save(resultById);
        }

        return null;
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product findByCode(String code) {
        return productRepository.findByCode(code);
    }

    @Override
    public List<Product> findAll() {
       return productRepository.findAll();
    }

    @Override
    public boolean remove(Long id) {
        try {
            Product productById = findById(id);
            if (productById == null) {
                throw new ResourceFoundException("Product not found");
            }

            productRepository.remove(id);
            return Boolean.TRUE;
        } catch (ResourceFoundException e) {
            log.error("Erro ao remover produto: {}", e.getMessage());
            return Boolean.FALSE;
        }
    }
}
