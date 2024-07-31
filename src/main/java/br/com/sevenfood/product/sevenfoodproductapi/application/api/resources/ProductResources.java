package br.com.sevenfood.product.sevenfoodproductapi.application.api.resources;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.ProductRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.response.ProductResponse;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.exception.ResourceFoundException;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.mappper.ProductApiMapper;
import br.com.sevenfood.product.sevenfoodproductapi.commons.Constants;
import br.com.sevenfood.product.sevenfoodproductapi.commons.util.RestUtils;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Product;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.product.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/products")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@CrossOrigin(origins = "*", allowedHeaders = "Content-Type, Authorization", maxAge = 3600)
public class ProductResources {

    private final CreateProductPort createProductPort;
    private final DeleteProductPort deleteProductPort;
    private final FindByIdProductPort findByIdProductPort;
    private final FindProductsPort findProductsPort;
    private final UpdateProductPort updateProductPort;
    private final ProductApiMapper productApiMapper;

    @Operation(summary = "Create a new Product", tags = {"products", "post"})
    @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = ProductResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponse> save(@Valid @RequestBody ProductRequest request) {
        try {
            log.info("Chegada do objeto para ser salvo {}", request);
            Product product = productApiMapper.fromRquest(request);
            Product saved = createProductPort.save(product);
            if (saved == null) {
                throw new ResourceFoundException("Produto não encontroado ao cadastrar");
            }

            ProductResponse productResponse = productApiMapper.fromEntidy(saved);
            URI location = RestUtils.getUri(productResponse.getId());

            return ResponseEntity.created(location).body(productResponse);
        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-save: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(summary = "Update a Product by Id", tags = {"products", "put"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ProductResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponse> update(@PathVariable("id") Long id, @Valid @RequestBody ProductRequest request) {
        try {
            log.info("Chegada do objeto para ser alterado {}", request);
            var product = productApiMapper.fromRquest(request);
            Product updated = updateProductPort.update(id, product);
            if (updated == null) {
                throw new ResourceFoundException("\"Produto não encontroado ao atualizar");
            }

            ProductResponse productResponse = productApiMapper.fromEntidy(updated);
            return ResponseEntity.ok(productResponse);
        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-update: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(summary = "Retrieve all Product", tags = {"products", "get", "filter"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ProductResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "204", description = "There are no Associations", content = {
            @Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<Product> productList = findProductsPort.findAll();
        List<ProductResponse> productResponse = productApiMapper.map(productList);
        return productResponse.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(productResponse);
    }

    @Operation(
            summary = "Retrieve a Product by Id",
            description = "Get a Product object by specifying its id. The response is Association object with id, title, description and published status.",
            tags = {"products", "get"})
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ProductResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductResponse> findOne(@PathVariable("id") Long id) {
        try {
            Product productSaved = findByIdProductPort.findById(id);
            if (productSaved == null) {
                throw new ResourceFoundException("Produto não encontrado ao buscar por id");
            }

            ProductResponse productResponse = productApiMapper.fromEntidy(productSaved);
            return ResponseEntity.ok(productResponse);
        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-findOne: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(
            summary = "Retrieve a Product by Id",
            description = "Get a Product object by specifying its id. The response is Association object with id, title, description and published status.",
            tags = {"products", "get"})
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ProductResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping("/code/{code}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductResponse> findByCode(@PathVariable("code") String code) {
        try {

            Product productSaved = findByIdProductPort.findByCode(code);
            if (productSaved == null) {
                throw new ResourceFoundException("Produto não encontrado ao buscar por código");
            }

            ProductResponse productResponse = productApiMapper.fromEntidy(productSaved);
            return ResponseEntity.ok(productResponse);
        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-findByCode: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(summary = "Delete a Product by Id", tags = {"producttrus", "delete"})
    @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> remove(@PathVariable("id") Long id) {
        deleteProductPort.remove(id);
        return ResponseEntity.noContent().build();
    }
}