package org.databaseservice.mapper;

import org.databaseservice.models.UserEntity;
import org.databaseservice.payload.RegisterDTO;
import org.databaseservice.payload.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    UserEntity registerDtoToUserEntity(RegisterDTO registerDTO);

    UserDTO userEntityToUserDTO(UserEntity userEntity);
}
