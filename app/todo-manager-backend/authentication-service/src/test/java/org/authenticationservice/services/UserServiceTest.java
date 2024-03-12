package org.authenticationservice.services;

import feign.FeignException;
import org.authenticationservice.clients.IDatabaseServiceClient;
import org.authenticationservice.payload.LoginDTO;
import org.authenticationservice.payload.TokenDTO;
import org.authenticationservice.payload.UserDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "eureka.client.registerWithEureka=false",
        "eureka.client.fetchRegistry=false"
})
@AutoConfigureMockMvc
class UserServiceTest {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private IJwtService jwtService;

    @MockBean
    private IDatabaseServiceClient databaseServiceClient;

    @Autowired
    private UserService userService;

    @Test
    void testAuthenticateUserSuccess() {
        LoginDTO loginDTO = new LoginDTO("john_doe", "P@ssw0rd");
        UserDTO userDTO = new UserDTO(1L, "john_doe", encoder.encode("P@ssw0rd"));

        Mockito.when(databaseServiceClient.getUserDTOFromUsername(loginDTO.getUsername()))
                .thenReturn(ResponseEntity.ok(userDTO));

        Optional<TokenDTO> result = userService.authenticateUser(loginDTO);

        assertTrue(result.isPresent());
    }

    @Test
    void testAuthenticateUserWithIncorrectPassword() {
        LoginDTO loginDTO = new LoginDTO("john_doe", "incorrectPassword");
        UserDTO userDTO = new UserDTO(1L, "john_doe", encoder.encode("P@ssw0rd"));

        Mockito.when(databaseServiceClient.getUserDTOFromUsername(loginDTO.getUsername()))
                .thenReturn(ResponseEntity.ok(userDTO));

        Optional<TokenDTO> result = userService.authenticateUser(loginDTO);

        assertFalse(result.isPresent());
    }

    @Test
    void testAuthenticateUserWhenUserNotFound() {
        LoginDTO loginDTO = new LoginDTO("unknown_user", "P@ssw0rd");

        Mockito.when(databaseServiceClient.getUserDTOFromUsername(loginDTO.getUsername()))
                .thenThrow(FeignException.NotFound.class);

        Optional<TokenDTO> result = userService.authenticateUser(loginDTO);

        assertFalse(result.isPresent());
    }

    @Test
    void testAuthenticateUserWhenResponseBodyIsNull() {
        LoginDTO loginDTO = new LoginDTO("john_doe", "P@ssw0rd");

        Mockito.when(databaseServiceClient.getUserDTOFromUsername(loginDTO.getUsername()))
                .thenReturn(ResponseEntity.ok(null));

        Optional<TokenDTO> result = userService.authenticateUser(loginDTO);

        assertFalse(result.isPresent());
    }
}
