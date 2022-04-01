package com.akvelon.dorodnikov.controllers;

import com.akvelon.dorodnikov.controllers.helpers.BaseController;
import com.akvelon.dorodnikov.domain.entites.ProductEntity;
import com.akvelon.dorodnikov.domain.entites.ProductPagination;
import com.akvelon.dorodnikov.domain.services.ProductService;
import com.akvelon.dorodnikov.dto.ProductDTO;
import com.akvelon.dorodnikov.dto.ProductPaginationDTO;
import com.akvelon.dorodnikov.utils.mappers.ProductMapper;
import com.akvelon.dorodnikov.utils.mappers.ProductPaginationMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for "Product" domain entity requests.
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    private final ProductPaginationMapper productPaginationMapper;

    /**
     * Returns ResponseEntity with HTTP status and list of {@link ProductDTO}-s.
     *
     * @param page Number of page.
     * @param size Limit of quantity of elements on one page.
     * @param sortDir Sort direction.
     * @param sort Parameter by which the list is sorted.
     * @return ResponseEntity with HTTP status and list of products.
     */
    @GetMapping
    public ResponseEntity getProducts(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortDir", defaultValue = "Asc") String sortDir,
            @RequestParam(value = "sort", defaultValue = "name") String sort) {
        List<ProductEntity> productEntityList = productService.getProducts(page, size, sortDir, sort);
        if (productEntityList != null) {
            return new ResponseEntity(productMapper.mapToProductDTOList(productEntityList), HttpStatus.OK);
        } else {
            return new ResponseEntity("The list of products was not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Returns ResponseEntity with HTTP status and {@link ProductPaginationDTO}.
     *
     * @param page Number of page.
     * @param size Limit of quantity of elements on one page.
     * @param sortDir Sort direction.
     * @param sort Parameter by which the list is sorted.
     * @return ResponseEntity with HTTP status and {@link ProductPaginationDTO}.
     */
    @GetMapping("/pagination")
    public ResponseEntity getProductPagination(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortDir", defaultValue = "Asc") String sortDir,
            @RequestParam(value = "sort", defaultValue = "name") String sort) {
        ProductPagination products = productService.getProductPagination(page, size, sortDir, sort);
        if (products != null) {
            return new ResponseEntity(productPaginationMapper.mapToProductPaginationDTO(products), HttpStatus.OK);
        }
        return new ResponseEntity("The list of products was not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Returns ResponseEntity with HTTP status and {@link ProductDTO}.
     *
     * @param id ID of ProductDTO to be received.
     * @return ResponseEntity with HTTP status and ProductDTO by ID.
     */
    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable int id) {
        ProductEntity productEntity = productService.get(id);
        if (productEntity != null) {
            return new ResponseEntity(productMapper.mapToProductDTO(productEntity), HttpStatus.OK);
        } else {
            return messageWithStatus("Product with ID " + id + " does not exist", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Creates new {@link ProductDTO}.
     *
     * @param productDto ProductDTO to be added.
     * @return ResponseEntity with HTTP status and the Product being saved in database.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@Valid @RequestBody ProductDTO productDto) {
        ProductEntity product = productMapper.mapToProductEntity(productDto);
        ProductEntity productCreated = productService.create(product);
        return new ResponseEntity(productMapper.mapToProductDTO(productCreated), HttpStatus.CREATED);
    }

    /**
     * Updates {@link ProductDTO}.
     *
     * @param id ID of product to be updated.
     * @param productDTO Object with data to update product in database.
     * @return ResponseEntity with HTTP status and the product being updated in database.
     */
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@PathVariable int id, @Valid @RequestBody ProductDTO productDTO) {
        ProductEntity productEntity = productMapper.mapToProductEntity(productDTO);
        ProductEntity productEntityUpdated = productService.update(id, productEntity);
        if (productEntityUpdated != null) {
            return new ResponseEntity(productMapper.mapToProductDTO(productEntityUpdated), HttpStatus.OK);
        }
        return messageWithStatus("Product with ID " + id + " does not exist", HttpStatus.BAD_REQUEST);
    }

    /**
     * Deletes {@link ProductDTO}.
     *
     * @param id ID of Product to be deleted.
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@PathVariable int id) {
        boolean present = productService.delete(id);
        if (present) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return messageWithStatus("Product with ID " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
    }
}