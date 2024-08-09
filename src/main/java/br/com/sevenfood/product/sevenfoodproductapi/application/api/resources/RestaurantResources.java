package br.com.sevenfood.product.sevenfoodproductapi.application.api.resources;

import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.request.RestaurantRequest;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.dto.response.RestaurantResponse;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.exception.ResourceFoundException;
import br.com.sevenfood.product.sevenfoodproductapi.application.api.mapper.RestaurantApiMapper;
import br.com.sevenfood.product.sevenfoodproductapi.commons.util.RestUtils;
import br.com.sevenfood.product.sevenfoodproductapi.core.domain.Restaurant;
import br.com.sevenfood.product.sevenfoodproductapi.core.ports.in.restaurant.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/restaurants")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@CrossOrigin(origins = "*", allowedHeaders = "Content-Type, Authorization", maxAge = 3600)
public class RestaurantResources {

    private final CreateRestaurantPort createRestaurantPort;
    private final DeleteRestaurantPort deleteRestaurantPort;
    private final FindByIdRestaurantPort findByIdRestaurantPort;
    private final FindRestaurantsPort findRestaurantsPort;
    private final UpdateRestaurantPort updateRestaurantPort;
    private final RestaurantApiMapper restaurantApiMapper;

    @Operation(summary = "Create a new Restaurant", tags = {"restaurants", "post"})
    @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = RestaurantResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RestaurantResponse> save(@Valid @RequestBody RestaurantRequest request) {
        log.info("Chegada do objeto para ser salvo {}", request);
        Restaurant restaurant = restaurantApiMapper.fromRequest(request);
        Restaurant saved = createRestaurantPort.save(restaurant);
        if (saved == null) {
            throw new ResourceFoundException("Produto não encontroado ao cadastrar");
        }

        RestaurantResponse restaurantResponse = restaurantApiMapper.fromEntity(saved);
        URI location = RestUtils.getUri(restaurantResponse.getId());
        return ResponseEntity.created(location).body(restaurantResponse);
    }

    @Operation(summary = "Update a Restaurant by Id", tags = {"restaurants", "put"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = RestaurantResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RestaurantResponse> update(@PathVariable("id") Long id, @Valid @RequestBody RestaurantRequest request) {
        log.info("Chegada do objeto para ser alterado {}", request);
        var restaurant = restaurantApiMapper.fromRequest(request);
        Restaurant updated = updateRestaurantPort.update(id, restaurant);
        if (updated == null) {
            throw new ResourceFoundException("Restaurante não encontroado ao atualizar");
        }

        RestaurantResponse restaurantResponse = restaurantApiMapper.fromEntity(updated);
        return ResponseEntity.ok(restaurantResponse);
    }

    @Operation(summary = "Retrieve all Restaurant", tags = {"restaurants", "get", "filter"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = RestaurantResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "204", description = "There are no Associations", content = {
            @Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        List<Restaurant> restaurantList = findRestaurantsPort.findAll();
        List<RestaurantResponse> restaurantResponse = restaurantApiMapper.map(restaurantList);
        return restaurantResponse.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(restaurantResponse);
    }

    @Operation(
            summary = "Retrieve a Restaurant by Id",
            description = "Get a Restaurant object by specifying its id. The response is Association object with id, title, description and published status.",
            tags = {"restaurants", "get"})
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = RestaurantResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RestaurantResponse> findOne(@PathVariable("id") Long id) {
        Restaurant restaurantSaved = findByIdRestaurantPort.findById(id);
        if (restaurantSaved == null) {
            throw new ResourceFoundException("Produto não encontrado ao buscar por código");
        }

        RestaurantResponse restaurantResponse = restaurantApiMapper.fromEntity(restaurantSaved);
        return ResponseEntity.ok(restaurantResponse);
    }

    @Operation(summary = "Delete a Restaurant by Id", tags = {"restauranttrus", "delete"})
    @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> remove(@PathVariable("id") Long id) {
        deleteRestaurantPort.remove(id);
        return ResponseEntity.noContent().build();
    }
}