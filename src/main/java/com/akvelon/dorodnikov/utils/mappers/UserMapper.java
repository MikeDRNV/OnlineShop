package com.akvelon.dorodnikov.utils.mappers;

import com.akvelon.dorodnikov.domain.entites.UserEntity;
import com.akvelon.dorodnikov.dto.UserDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Provides conversion for {@link UserEntity} and for {@link UserDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    /**
     * Converts {@link UserEntity} to {@link UserDTO} based on the userEntity object.
     *
     * @param userEntity Provides data for conversion.
     * @return UserDTO object based on the object of {@link UserEntity} in the parameter.
     */
    @Mapping(target = "isDeleted", source = "userEntity.deleted")
    UserDTO userEntityToUserDTO(UserEntity userEntity);

    /**
     * Converts {@link UserDTO} to {@link UserEntity} based on the userDTO object.
     *
     * @param userDTO Provides data for conversion.
     * @return UserEntity object based on the object of {@link UserDTO} in the parameter.
     */
    @Mapping(target = "deleted", source = "userDTO.isDeleted")
    UserEntity userDTOToUserEntity(UserDTO userDTO);

    /**
     * Converts list of {@link UserEntity} to list of {@link UserDTO}.
     *
     * @param userEntities Provides data for conversion.
     * @return List of UserDTO based on the list of {@link UserEntity} in the parameter.
     */
    List<UserDTO> mapToUserDTOList(List<UserEntity> userEntities);
}