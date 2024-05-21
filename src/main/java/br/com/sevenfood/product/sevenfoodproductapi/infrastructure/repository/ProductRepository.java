package br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository;

import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    ProductEntity findByCode(String code);
}
