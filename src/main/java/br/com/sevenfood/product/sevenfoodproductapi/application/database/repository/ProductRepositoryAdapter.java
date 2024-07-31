package br.com.sevenfood.product.sevenfoodproductapi.application.database.repository;

import br.com.sevenfood.product.sevenfoodproductapi.application.database.mapper.ProductMapper;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.out.ProductRepositoryPort;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Product save(Product product) {
        try {
            ProductEntity productEntity = productMapper.fromModelTpEntity(product);
            productEntity.setCode(UUID.randomUUID().toString());
            ProductEntity saved = productRepository.save(productEntity);
            return productMapper.fromEntityToModel(saved);
        } catch (Exception e) {
            log.info("Erro ao salvar produto: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean remove(Long id) {
         try {
            productRepository.deleteById(id);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public Product findById(Long id) {
        Optional<ProductEntity> buProduct = productRepository.findById(id);
        if (buProduct.isPresent()) {
            return productMapper.fromEntityToModel(buProduct.get());
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        List<ProductEntity> all = productRepository.findAll();
        return productMapper.map(all);
    }

    @Override
    public Product update(Long id, Product product) {
        Optional<ProductEntity> resultById = productRepository.findById(id);
        if (resultById.isPresent()) {

            ProductEntity productToChange = resultById.get();
            productToChange.update(id, productToChange);

            return productMapper.fromEntityToModel(productRepository.save(productToChange));
        }

        return null;
    }

    @Override
    public Product findByCode(String code) {
        ProductEntity byCode = productRepository.findByCode(code);
        return productMapper.fromEntityToModel(byCode);
    }
}
