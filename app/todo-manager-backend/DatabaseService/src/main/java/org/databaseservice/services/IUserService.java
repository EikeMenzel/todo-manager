package org.databaseservice.services;

import org.databaseservice.payload.RegisterDTO;
import org.databaseservice.payload.UserDTO;

import java.util.Optional;

public interface IUserService {
    boolean existsUserByUsername(String username);
    void saveUser(RegisterDTO registerDTO);
    Optional<UserDTO> getUserFromUsername(String username);
}
