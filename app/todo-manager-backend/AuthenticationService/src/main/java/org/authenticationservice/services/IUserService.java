package org.authenticationservice.services;

import org.authenticationservice.payload.LoginDTO;
import org.authenticationservice.payload.TokenDTO;

import java.util.Optional;

public interface IUserService {
    String encodePassword(String password);
    Optional<TokenDTO> authenticateUser(LoginDTO loginDTO);
}
