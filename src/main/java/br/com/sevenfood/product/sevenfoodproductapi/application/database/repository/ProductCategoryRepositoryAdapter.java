package br.com.sevenfood.product.sevenfoodproductapi.application.database.repository;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.exception.ResourceFoundException;
import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.ProductCategoryMapper;
import br.com.sevenfood.product.sevenfoodproductapi.commons.exception.ResourceNotRemoveException;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.out.ProductCategoryRepositoryPort;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductCategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductCategoryRepositoryAdapter implements ProductCategoryRepositoryPort {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        try {
            ProductCategoryEntity productCategoryEntity = productCategoryMapper.fromModelTpEntity(productCategory);
            ProductCategoryEntity saved = productCategoryRepository.save(productCategoryEntity);
            validateSavedEntity(saved);
            return productCategoryMapper.fromEntityToModel(saved);
        } catch (ResourceFoundException e) {
            log.error("Erro ao salvar produto: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean remove(Long id) {
         try {
             productCategoryRepository.deleteById(id);
            return Boolean.TRUE;
        } catch (ResourceNotRemoveException e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public ProductCategory findById(Long id) {
        Optional<ProductCategoryEntity> buProductCategory = productCategoryRepository.findById(id);
        if (buProductCategory.isPresent()) {
            return productCategoryMapper.fromEntityToModel(buProductCategory.get());
        }
        return null;
    }

    @Override
    public List<ProductCategory> findAll() {
        List<ProductCategoryEntity> all = productCategoryRepository.findAll();
        return productCategoryMapper.map(all);
    }

    @Override
    public ProductCategory update(Long id, ProductCategory productCategory) {
        Optional<ProductCategoryEntity> resultById = productCategoryRepository.findById(id);
        if (resultById.isPresent()) {
            ProductCategoryEntity productCategoryToChange = resultById.get();
            productCategoryToChange.update(id, productCategoryToChange);

            return productCategoryMapper.fromEntityToModel(productCategoryRepository.save(productCategoryToChange));
        }
        return null;
    }

    private void validateSavedEntity(ProductCategoryEntity saved) {
        if (saved == null) {
            throw new ResourceFoundException("Erro ao salvar produto no repositorio: entidade salva é nula");
        }

        if (saved.getName() == null) {
            throw new ResourceFoundException("Erro ao salvar produto no repositorio: nome do produto é nulo");
        }
    }
}
