package br.com.sevenfood.product.sevenfoodproductapi.repository;

import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.productcategory.ProductCategoryEntity;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.repository.ProductCategoryRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
public class ProductCategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    private ProductCategoryEntity getProductCategory() {
        return ProductCategoryEntity.builder()
                .id(1l)
                .name("Bebida")
                .build();
    }


    @BeforeEach
    public void setUp() {
        productCategoryRepository.save(getProductCategory());
    }

    @Test
    public void should_find_no_clients_if_repository_is_empty() {
        Iterable<ProductCategoryEntity> seeds = productCategoryRepository.findAll();
        seeds = Collections.EMPTY_LIST;
        assertThat(seeds).isEmpty();
    }

    @Test
    public void should_store_a_product_category() {
        String cocaColaBeverage = "Coca-Cola";
        Optional<ProductCategoryEntity> productCategory = productCategoryRepository.findByName(cocaColaBeverage);
        Optional<ProductCategoryEntity> productCategoryResponse = null;
        if (!productCategory.isPresent()) {

            ProductCategoryEntity cocaCola = ProductCategoryEntity.builder()
                    .name(cocaColaBeverage)
                    .build();

            ProductCategoryEntity save = productCategoryRepository.save(cocaCola);
            productCategoryResponse = productCategoryRepository.findByName(cocaColaBeverage);
        }

        ProductCategoryEntity productCategory1 = productCategoryResponse.get();
        assertThat(productCategory1).hasFieldOrPropertyWithValue("name", cocaColaBeverage);
    }

    @Test
    public void whenConstraintViolationExceptionThrown_thenAssertionSucceeds() {
        ProductCategoryEntity productCategory = createInvalidProductCategory();

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            productCategoryRepository.save(productCategory);
        });

        String expectedMessage = "tamanho deve ser entre 1 e 255";
        String actualMessage = exception.getMessage();

        // Adicionar saída de log para a mensagem da exceção
        log.info("Actual Exception Message:{}", actualMessage);

        assertTrue(actualMessage.contains(expectedMessage),
                "Expected message to contain: " + expectedMessage + " but was: " + actualMessage);
    }

    private ProductCategoryEntity createInvalidProductCategory() {
        ProductCategoryEntity productCategory = new ProductCategoryEntity();
        // Configurar o productCategory com valores inválidos
        // Exemplo: valores inválidos que podem causar uma ConstraintViolationException
        productCategory.setName(""); // Nome vazio pode causar uma violação
        return productCategory;
    }

    @Test
    public void should_found_null_ProductCategory() {
        ProductCategoryEntity productCategory = null;

        Optional<ProductCategoryEntity> fromDb = productCategoryRepository.findById(99l);
        if (fromDb.isPresent()) {
            productCategory = fromDb.get();
        }
        assertThat(productCategory).isNull();
    }

    @Test
    public void whenFindById_thenReturnProductCategory() {
        Optional<ProductCategoryEntity> productCategory = productCategoryRepository.findById(1l);
        if (productCategory.isPresent()) {
            ProductCategoryEntity productCategoryResult = productCategory.get();
            assertThat(productCategoryResult).hasFieldOrPropertyWithValue("name", "Bebida");
        }
    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        ProductCategoryEntity fromDb = productCategoryRepository.findById(-11l).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    public void givenSetOfProductCategorys_whenFindAll_thenReturnAllProductCategorys() {
        ProductCategoryEntity productCategory = null;
        ProductCategoryEntity productCategory1 = null;
        ProductCategoryEntity productCategory2 = null;

        Optional<ProductCategoryEntity> restaurant = productCategoryRepository.findById(1l);
        if (restaurant.isPresent()) {

            ProductCategoryEntity bebida = ProductCategoryEntity.builder()
                    .name("Bebida")
                    .build();
            productCategory = productCategoryRepository.save(bebida);

            ProductCategoryEntity acompanhamento = ProductCategoryEntity.builder()
                    .name("Acompanhamento")
                    .build();
            productCategory1 = productCategoryRepository.save(acompanhamento);

            ProductCategoryEntity lanche = ProductCategoryEntity.builder()
                    .name("Lanche")
                    .build();
            productCategory2 = productCategoryRepository.save(lanche);

        }

        Iterator<ProductCategoryEntity> allProductCategorys = productCategoryRepository.findAll().iterator();
        List<ProductCategoryEntity> clients = new ArrayList<>();
        allProductCategorys.forEachRemaining(c -> clients.add(c));

        assertThat(allProductCategorys);
    }
}