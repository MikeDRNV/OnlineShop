package com.akvelon.dorodnikov.utils.mappers;

import com.akvelon.dorodnikov.domain.entites.ProductEntity;
import com.akvelon.dorodnikov.dto.ProductDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Provides conversion for {@link ProductEntity} and for {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Converts {@link ProductEntity} to {@link ProductDTO}.
     *
     * @param productEntity Provides data for conversion.
     * @return ProductDTO object based on the object of {@link ProductEntity} in the parameter.
     */
    ProductDTO mapToProductDTO(ProductEntity productEntity);

    /**
     * Converts {@link ProductDTO} to {@link ProductEntity}.
     *
     * @param productDTO Provides data for conversion.
     * @return ProductEntity object based on the object of {@link ProductDTO} in the parameter.
     */
    @Mapping(target = "id", ignore = true)
    ProductEntity mapToProductEntity(ProductDTO productDTO);

    /**
     * Converts list of {@link ProductEntity} to list of {@link ProductDTO}-s.
     *
     * @param productEntityList Provides data for conversion.
     * @return List of ProductDTO based on the list of {@link ProductEntity} in the parameter.
     */
    List<ProductDTO> mapToProductDTOList(List<ProductEntity> productEntityList);
}