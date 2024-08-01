package br.com.sevenfood.product.sevenfoodproductapi.domain;

import br.com.sevenfood.product.sevenfoodproductapi.commons.Constants;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.domain.AuditDomain;
import br.com.sevenfood.product.sevenfoodproductapi.infrastructure.entity.domain.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuditDomainTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testDefaultValues() {
        AuditDomain auditDomain = new AuditDomain();

        assertNotNull(auditDomain.getCreatedDate());
        assertEquals(Constants.CURRENT_USER, auditDomain.getCreateBy());
        assertEquals(Status.ATIVO, auditDomain.getStatus());
    }

    @Test
    void testNotNullValidation() {
        AuditDomain auditDomain = new AuditDomain();
        auditDomain.setCreatedDate(null);
        auditDomain.setCreateBy(null);
        auditDomain.setStatus(null);

        Set<ConstraintViolation<AuditDomain>> violations = validator.validate(auditDomain);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testLastModifiedFields() {
        AuditDomain auditDomain = new AuditDomain();
        LocalDateTime now = LocalDateTime.now();
        String user = "testUser";

        auditDomain.setLastModifiedDate(now);
        auditDomain.setLastModifiedBy(user);

        assertEquals(now, auditDomain.getLastModifiedDate());
        assertEquals(user, auditDomain.getLastModifiedBy());
    }

    @Test
    void testGettersAndSetters() {
        AuditDomain auditDomain = new AuditDomain();

        LocalDateTime now = LocalDateTime.now();
        auditDomain.setCreatedDate(now);
        auditDomain.setCreateBy("testUser");
        auditDomain.setLastModifiedDate(now);
        auditDomain.setLastModifiedBy("testModifier");
        auditDomain.setStatus(Status.INATIVO);

        assertEquals(now, auditDomain.getCreatedDate());
        assertEquals("testUser", auditDomain.getCreateBy());
        assertEquals(now, auditDomain.getLastModifiedDate());
        assertEquals("testModifier", auditDomain.getLastModifiedBy());
        assertEquals(Status.INATIVO, auditDomain.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        AuditDomain auditDomain1 = new AuditDomain();
        auditDomain1.setCreateBy("user");
        auditDomain1.setCreatedDate(LocalDateTime.now());
        auditDomain1.setStatus(Status.ATIVO);

        AuditDomain auditDomain2 = new AuditDomain();
        auditDomain2.setCreateBy("user");
        auditDomain2.setCreatedDate(auditDomain1.getCreatedDate());
        auditDomain2.setStatus(Status.ATIVO);

        assertEquals(auditDomain1, auditDomain2);
        assertEquals(auditDomain1.hashCode(), auditDomain2.hashCode());
    }

    @Test
    void testToString() {
        AuditDomain auditDomain = new AuditDomain();
        auditDomain.setCreateBy("user");
        auditDomain.setCreatedDate(LocalDateTime.of(2024, 7, 31, 12, 0));
        auditDomain.setStatus(Status.ATIVO);

        String expectedString = "AuditDomain(createdDate=2024-07-31T12:00, createBy=user, lastModifiedDate=null, lastModifiedBy=null, status=ATIVO)";
        assertEquals(expectedString, auditDomain.toString());
    }
}
