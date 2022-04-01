package com.akvelon.dorodnikov.utils.mappers;

import com.akvelon.dorodnikov.domain.entites.UserDetails;
import com.akvelon.dorodnikov.dto.UserDetailsDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Provides conversion {@link UserDetails} to {@link UserDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserDetailsMapper {

    /**
     * Converts {@link UserDetails} to {@link UserDetailsDTO}.
     *
     * @param userDetails Provides data for conversion.
     * @return UserDetailsDTO object based on the object of {@link UserDetails} in the parameter.
     */
    @Mappings({
            @Mapping(target = "id", source = "userEntity.id"),
            @Mapping(target = "firstname", source = "userEntity.firstname"),
            @Mapping(target = "lastname", source = "userEntity.lastname"),
            @Mapping(target = "email", source = "userEntity.email"),
            @Mapping(target = "password", source = "userEntity.password"),
            @Mapping(target = "isDeleted", source = "userEntity.deleted"),
    })
    UserDetailsDTO mapToUserDetailsDTO(UserDetails userDetails);
}