package org.authenticationservice.services;

public interface IJwtService {
    String generateToken(Long userId);
}
