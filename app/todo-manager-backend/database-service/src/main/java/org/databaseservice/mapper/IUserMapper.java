package org.databaseservice.mapper;

import org.databaseservice.models.UserEntity;
import org.databaseservice.payload.RegisterDTO;
import org.databaseservice.payload.UserDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    UserEntity registerDtoToUserEntity(RegisterDTO registerDTO);

    UserDTO UserEntityToUserDTO(UserEntity userEntity);
}
