package com.akvelon.dorodnikov.utils.mappers;

import com.akvelon.dorodnikov.domain.entites.CartEntity;
import com.akvelon.dorodnikov.domain.entites.UserEntity;
import com.akvelon.dorodnikov.dto.CartDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * Provides conversion for {@link CartEntity} and for {@link CartDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {

    /**
     * Converts {@link CartDTO} to {@link CartEntity}.
     *
     * @param cartDTO Provides data for conversion.
     * @return UserEntity object based on the object of {@link CartDTO} in the parameter.
     */
    @Mappings({
            @Mapping(target = "user", source = "userId"),
            @Mapping(target = "completed", source = "cartDTO.isCompleted")
    })
    CartEntity toCartEntity(CartDTO cartDTO);

    /**
     * Converts {@link CartEntity} to {@link CartDTO}.
     *
     * @param cartEntity Provides data for conversion.
     * @return UserDTO object based on the object of {@link CartEntity} in the parameter.
     */
    @Mappings({
            @Mapping(target = "userId", source = "cartEntity.user.id"),
            @Mapping(target = "isCompleted", source = "cartEntity.completed")
    })
    CartDTO toCartDTO(CartEntity cartEntity);

    /**
     * Converts ID of user to {@link UserEntity}.
     *
     * @param id ID of user.
     * @return {@link UserEntity} object.
     */
    UserEntity map(Integer id);
}