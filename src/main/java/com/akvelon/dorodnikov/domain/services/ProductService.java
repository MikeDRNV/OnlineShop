package com.akvelon.dorodnikov.domain.services;

import com.akvelon.dorodnikov.domain.entites.ProductEntity;
import com.akvelon.dorodnikov.domain.entites.ProductPagination;
import com.akvelon.dorodnikov.utils.exceptions.PaginationException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationPageException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationSizeException;
import com.akvelon.dorodnikov.utils.exceptions.PaginationSortException;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Defines the behavior for {@link ProductEntity}.
 */
@Service
public interface ProductService {

    /**
     * Returns list of {@link ProductEntity}-s.
     *
     * @param page Number of page.
     * @param size Limit of quantity of elements on one page.
     * @param sortDir Sort direction.
     * @param sort Parameter by which the list is sorted.
     * @return List of products.
     */
    List<ProductEntity> getProducts(int page, int size, String sortDir, String sort);

    /**
     * Returns {@link ProductPagination}.
     *
     * @param page Number of page.
     * @param size Limit of quantity of elements on one page.
     * @param sortDir Sort direction.
     * @param sort Parameter by which the list is sorted.
     * @return {@link ProductPagination} object.
     * @throws PaginationException when trying to sort by an incorrect direction and when trying to get a page and the
     * page size is less than one.
     * @throws PaginationSortException when trying to sort by a non-existent parameter.
     * @throws PaginationPageException when trying to get a page whose number is greater than the total number of
     * pages.
     * @throws PaginationSizeException when trying to get a page size that is larger than the total number of elements.
     */
    ProductPagination getProductPagination(int page, int size, String sortDir, String sort) throws PaginationException;

    /**
     * Returns {@link ProductEntity}.
     *
     * @param id ID of ProductEntity to be received.
     * @return ProductEntity by ID.
     */
    ProductEntity get(int id);

    /**
     * Creates new {@link ProductEntity}.
     *
     * @param productEntity ProductEntity to be added.
     * @return ProductEntity being saved in database.
     */
    ProductEntity create(ProductEntity productEntity);

    /**
     * Updates {@link ProductEntity}.
     *
     * @param id ID of ProductEntity to be updated.
     * @param productEntity Object containing the data to update.
     * @return ProductEntity being updated in database.
     */
    ProductEntity update(Integer id, ProductEntity productEntity);

    /**
     * Deletes {@link ProductEntity}.
     *
     * @param id ID of ProductEntity to be deleted.
     * @return {@code False} if ID do not exist, else return {@code true}.
     */
    boolean delete(Integer id);
}