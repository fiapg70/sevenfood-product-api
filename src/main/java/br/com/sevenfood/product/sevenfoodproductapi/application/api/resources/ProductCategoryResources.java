package br.com.sevenfood.product.sevenfoodproductapi.application.api.resources;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.ProductCategoryRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.response.ProductCategoryResponse;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.exception.ResourceFoundException;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.mapper.ProductCategoryApiMapper;
import br.com.sevenfood.product.sevenfoodproductapi.commons.Constants;
import br.com.sevenfood.product.sevenfoodproductapi.commons.util.RestUtils;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.ProductCategory;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.productcategory.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/product-categories")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@CrossOrigin(origins = "*", allowedHeaders = "Content-Type, Authorization", maxAge = 3600)
public class ProductCategoryResources {

    private final CreateProductCategoryPort createProductCategoryPort;
    private final DeleteProductCategoryPort deleteProductCategoryPort;
    private final FindByIdProductCategoryPort findByIdProductCategoryPort;
    private final FindProductCategoriesPort findProductCategoriesPort;
    private final UpdateProductCategoryPort updateProductCategoryPort;
    private final ProductCategoryApiMapper productCategoryApiMapper;

    @Operation(summary = "Create a new ProductCategory", tags = {"productCategorys", "post"})
    @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = ProductCategoryResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductCategoryResponse> save(@Valid @RequestBody ProductCategoryRequest request) {
        try {
            log.info("Chegada do objeto para ser salvo {}", request);
            ProductCategory productCategory = productCategoryApiMapper.fromRequest(request);
            ProductCategory saved = createProductCategoryPort.save(productCategory);
            if (saved == null) {
                throw new ResourceFoundException("Produto não encontroado ao cadastrar");
            }

            ProductCategoryResponse productCategoryResponse = productCategoryApiMapper.fromEntity(saved);
            URI location = RestUtils.getUri(productCategoryResponse.getId());
            return ResponseEntity.created(location).body(productCategoryResponse);
        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-save: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(summary = "Update a ProductCategory by Id", tags = {"productCategorys", "put"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ProductCategoryResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductCategoryResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ProductCategoryRequest request) {
        try {
            log.info("Chegada do objeto para ser alterado {}", request);
            var productCategory = productCategoryApiMapper.fromRequest(request);
            ProductCategory updated = updateProductCategoryPort.update(id, productCategory);
            if (updated == null) {
                throw new ResourceFoundException("\"Produto não encontroado ao atualizar");
            }

            ProductCategoryResponse productCategoryResponse = productCategoryApiMapper.fromEntity(updated);
            return ResponseEntity.ok(productCategoryResponse);
        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-update: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(summary = "Retrieve all ProductCategory", tags = {"productCategorys", "get", "filter"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ProductCategoryResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "204", description = "There are no Associations", content = {
            @Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductCategoryResponse>> findAll() {
        List<ProductCategory> productCategoryList = findProductCategoriesPort.findAll();
        List<ProductCategoryResponse> productCategoryResponse = productCategoryApiMapper.map(productCategoryList);
        return productCategoryResponse.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(productCategoryResponse);
    }

    @Operation(
            summary = "Retrieve a ProductCategory by Id",
            description = "Get a ProductCategory object by specifying its id. The response is Association object with id, title, description and published status.",
            tags = {"productCategorys", "get"})
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ProductCategoryResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductCategoryResponse> findOne(@PathVariable("id") Long id) {
        try {
            ProductCategory productCategorySaved = findByIdProductCategoryPort.findById(id);
            if (productCategorySaved == null) {
                throw new ResourceFoundException("Produto não encontrado ao buscar por código");
            }

            ProductCategoryResponse productCategoryResponse = productCategoryApiMapper.fromEntity(productCategorySaved);
            return ResponseEntity.ok(productCategoryResponse);

        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-findOne: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(summary = "Delete a ProductCategory by Id", tags = {"productCategorytrus", "delete"})
    @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> remove(@PathVariable("id") Long id) {
        deleteProductCategoryPort.remove(id);
        return ResponseEntity.noContent().build();
    }
}