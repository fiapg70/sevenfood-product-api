package br.com.sevenfood.product.sevenfoodproductapi.repository.impl;

import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    private ProductCategoryEntity electronics;
    private ProductCategoryEntity books;

    @BeforeEach
    void setUp() {
        electronics = new ProductCategoryEntity();
        electronics.setName("Electronics");

        books = new ProductCategoryEntity();
        books.setName("Books");

        productCategoryRepository.save(electronics);
        productCategoryRepository.save(books);
    }

    @Test
    void testFindByName_found() {
        Optional<ProductCategoryEntity> found = productCategoryRepository.findByName("Electronics");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Electronics");
    }

    @Test
    void testFindByName_notFound() {
        Optional<ProductCategoryEntity> found = productCategoryRepository.findByName("NonExistingCategory");
        assertThat(found).isNotPresent();
    }

    @Test
    void testFindByName_caseInsensitive() {
        Optional<ProductCategoryEntity> found = productCategoryRepository.findByName("electronics");
        assertThat(found).isNotPresent();
    }
}
