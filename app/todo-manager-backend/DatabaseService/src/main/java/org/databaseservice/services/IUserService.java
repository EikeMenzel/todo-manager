package org.databaseservice.services;

import org.apache.catalina.User;
import org.databaseservice.payload.RegisterDTO;

public interface IUserService {
    boolean existsUserByUsername(String username);
    boolean saveUser(RegisterDTO registerDTO);
}
