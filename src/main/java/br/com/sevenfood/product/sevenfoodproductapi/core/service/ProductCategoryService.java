package br.com.sevenfood.product.sevenfoodproductapi.core.service;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.exception.ResourceFoundException;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.productcategory.*;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.out.ProductCategoryRepositoryPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductCategoryService implements CreateProductCategoryPort, UpdateProductCategoryPort, FindByIdProductCategoryPort, FindProductCategoriesPort, DeleteProductCategoryPort {

    private final ProductCategoryRepositoryPort productCategoryRepository;

    @Override
    public ProductCategory save(ProductCategory product) {
        return productCategoryRepository.save(product);
    }

    @Override
    public ProductCategory update(Long id, ProductCategory product) {
        ProductCategory resultById = findById(id);
        if (resultById != null) {
            resultById.update(id, product);

            return productCategoryRepository.save(resultById);
        }

        return null;
    }

    @Override
    public ProductCategory findById(Long id) {
        return productCategoryRepository.findById(id);
    }

    @Override
    public List<ProductCategory> findAll() {
       return productCategoryRepository.findAll();
    }

    @Override
    public boolean remove(Long id) {
        try {
            ProductCategory productCategoryById = findById(id);
            if (productCategoryById == null) {
                throw new ResourceFoundException("Product Category not found");
            }

            productCategoryRepository.remove(id);
            return Boolean.TRUE;
        } catch (ResourceFoundException e) {
            log.error("Erro ao remover produto: {}", e.getMessage());
            return Boolean.FALSE;
        }
    }
}
