package com.akvelon.dorodnikov.domain.services;

import com.akvelon.dorodnikov.domain.entites.ProductEntity;
import com.akvelon.dorodnikov.domain.entites.ProductPagination;
import com.akvelon.dorodnikov.domain.repositories.ProductRepository;
import com.akvelon.dorodnikov.utils.exceptions.PaginationException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationPageException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationSizeException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationSortException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

/**
 * Provides business logic for {@link ProductEntity}.
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductEntity> getProducts(int page, int size, String sortDir, String sort) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sort));
        Page<ProductEntity> products = productRepository.findAll(pageRequest);
        return products.getContent();
    }

    @Override
    public ProductPagination getProductPagination(int page, int size, String sortDir, String sort)
            throws PaginationException {
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sort));
            Page<ProductEntity> products = productRepository.findAll(pageRequest);
            if (page > products.getTotalPages()) {
                throw new PaginationPageException(String.valueOf(page));
            } else if (size > products.getTotalElements()) {
                throw new PaginationSizeException(String.valueOf(size));
            }
            ProductPagination productPagination = new ProductPagination();
            productPagination.setTotalItems(products.getTotalElements());
            productPagination.setProductEntities(products.getContent());
            productPagination.setTotalPages(products.getTotalPages());
            productPagination.setCurrentPage(products.getNumber());
            return productPagination;
        } catch (IllegalArgumentException e) {
            throw new PaginationException(e.getMessage());
        } catch (PropertyReferenceException e) {
            throw new PaginationSortException(e.getPropertyName());
        }
    }

    @Override
    public ProductEntity get(int id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public ProductEntity create(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    @Override
    public ProductEntity update(Integer id, ProductEntity newProductEntity) {
        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
        if (productEntityOptional.isPresent()) {
            ProductEntity productEntity = productEntityOptional.get();
            productEntity.setName(newProductEntity.getName());
            productEntity.setShortDescription(newProductEntity.getShortDescription());
            productEntity.setFullDescription(newProductEntity.getFullDescription());
            productEntity.setImgLink(newProductEntity.getImgLink());
            productEntity.setPrice(newProductEntity.getPrice());
            return productRepository.save(productEntity);
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}