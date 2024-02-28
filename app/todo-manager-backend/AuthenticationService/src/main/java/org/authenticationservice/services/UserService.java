package org.authenticationservice.services;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.authenticationservice.clients.IDatabaseServiceClient;
import org.authenticationservice.payload.LoginDTO;
import org.authenticationservice.payload.TokenDTO;
import org.authenticationservice.payload.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService{
    private final PasswordEncoder encoder;
    private final IJwtService jwtService;
    private final IDatabaseServiceClient databaseServiceClient;
    public String encodePassword(String password) {
        return encoder.encode(password);
    }

    public Optional<TokenDTO> authenticateUser(LoginDTO loginDTO) { //returns a jwt if authentication is successful
        try {
            ResponseEntity<UserDTO> responseEntity = databaseServiceClient.getUserDTOFromUsername(loginDTO.getUsername());
            if(!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null)
                return Optional.empty();

            val user = responseEntity.getBody();
            boolean arePasswordsEqual = encoder.matches(loginDTO.getPassword(), user.getPassword());
            return arePasswordsEqual
                    ? Optional.of(new TokenDTO(jwtService.generateToken(user.getId())))
                    : Optional.empty();
        } catch (FeignException e) { // If a user is not found, we still want to return a 401!
            log.debug(e.getMessage());
            return Optional.empty();
        }
    }
}
