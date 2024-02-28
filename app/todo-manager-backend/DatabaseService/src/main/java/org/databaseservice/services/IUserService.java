package org.databaseservice.services;

import org.apache.catalina.User;
import org.databaseservice.payload.RegisterDTO;
import org.databaseservice.payload.UserDTO;

import java.util.Optional;

public interface IUserService {
    boolean existsUserByUsername(String username);
    boolean saveUser(RegisterDTO registerDTO);

    Optional<UserDTO> getUserFromUsername(String username);
}
