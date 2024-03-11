package org.authenticationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.authenticationservice.clients.IDatabaseServiceClient;
import org.authenticationservice.constants.ErrorMessage;
import org.authenticationservice.exceptions.PasswordNotSecureException;
import org.authenticationservice.payload.LoginDTO;
import org.authenticationservice.payload.RegisterDTO;
import org.authenticationservice.payload.TokenDTO;
import org.authenticationservice.payload.UserDTO;
import org.authenticationservice.services.IPasswordService;
import org.authenticationservice.services.IUserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "eureka.client.registerWithEureka=false",
        "eureka.client.fetchRegistry=false"
})
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserService userService;

    @MockBean
    private IDatabaseServiceClient databaseServiceClient;

    @Autowired
    private IPasswordService passwordService;

    @InjectMocks
    private AuthenticationController authenticationController;


    @Autowired
    private PasswordEncoder encoder;

    @Test
    void testRegisterUserSuccessfully() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO("newUser", "P@ssw0rd123");

        Mockito.when(databaseServiceClient.existsUsernameInDatabase(registerDTO.getUsername())).thenReturn(false);

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isOk());

        ArgumentCaptor<RegisterDTO> argumentCaptor = ArgumentCaptor.forClass(RegisterDTO.class);
        Mockito.verify(databaseServiceClient, Mockito.times(1)).saveUser(argumentCaptor.capture());

        RegisterDTO capturedRegisterDTO = argumentCaptor.getValue();

        assertEquals("newUser", capturedRegisterDTO.getUsername());
        assertNotNull(capturedRegisterDTO.getPassword());
        assertNotEquals("P@ssw0rd123", capturedRegisterDTO.getPassword());
    }

    @Test
    void testRegisterUserWithInsecurePassword() throws Exception {
        String insecurePassword = "lowercaseonly";
        RegisterDTO registerDTO = new RegisterDTO("insecureUser", insecurePassword);

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(PasswordNotSecureException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(ErrorMessage.PASSWORD_CONSTRAINTS,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        Mockito.verify(databaseServiceClient, Mockito.never()).saveUser(any(RegisterDTO.class));
    }

    @Test
    void testRegisterUserWithExistingUsername() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO("existingUser", "P@ssw0rd123");

        Mockito.when(databaseServiceClient.existsUsernameInDatabase(registerDTO.getUsername())).thenReturn(true);

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isConflict());

        Mockito.verify(databaseServiceClient, Mockito.never()).saveUser(any(RegisterDTO.class));
    }

    @Test
    void testRegisterUserWithoutUsername() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO("", "P@ssw0rd123"); // Empty username

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithShortUsername() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO("user", "P@ssw0rd123"); // Username too short

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithLongUsername() throws Exception {
        String longUsername = "username_is_way_too_long_and_exceeds_the_maximum_allowed_length";
        RegisterDTO registerDTO = new RegisterDTO(longUsername, "P@ssw0rd123"); // Username too long

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithoutPassword() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO("newUser", ""); // Empty password

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithShortPassword() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO("newUser", "short"); // Password too short

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUserWithLongPassword() throws Exception {
        String longPassword = "this_password_is_way_too_long_and_exceeds_the_maximum_allowed_length_which_is_unacceptable";
        RegisterDTO registerDTO = new RegisterDTO("newUser", longPassword); // Password too long

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginUserSuccessfully() throws Exception {
        LoginDTO loginDTO = new LoginDTO("john_doe", "P@ssw0rd");

        UserDTO userDTO = new UserDTO(1L, "john_doe", encoder.encode("P@ssw0rd"));

        ResponseEntity<UserDTO> responseEntity = ResponseEntity.ok(userDTO);
        Mockito.when(databaseServiceClient.getUserDTOFromUsername(loginDTO.getUsername()))
                .thenReturn(responseEntity);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO("john_doe", "wrongPassword");
        UserDTO userDTO = new UserDTO(1L, "john_doe", "encodedPassword");

        ResponseEntity<UserDTO> responseEntity = ResponseEntity.ok(userDTO);
        Mockito.when(databaseServiceClient.getUserDTOFromUsername(loginDTO.getUsername()))
                .thenReturn(responseEntity);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void testLoginWithoutUsername() throws Exception {
        LoginDTO loginDTO = new LoginDTO("", "P@ssw0rd"); // Empty username

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithoutPassword() throws Exception {
        LoginDTO loginDTO = new LoginDTO("john_doe", ""); // Empty password

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }
}

