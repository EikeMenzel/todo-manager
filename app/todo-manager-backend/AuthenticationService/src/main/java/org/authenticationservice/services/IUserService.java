package org.authenticationservice.services;

import org.authenticationservice.payload.LoginDTO;

import java.util.Optional;

public interface IUserService {
    String encodePassword(String password);
    Optional<String> authenticateUser(LoginDTO loginDTO);
}
