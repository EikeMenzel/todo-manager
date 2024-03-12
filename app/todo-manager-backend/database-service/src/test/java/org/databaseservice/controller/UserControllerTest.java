package org.databaseservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.databaseservice.payload.RegisterDTO;
import org.databaseservice.payload.UserDTO;
import org.databaseservice.services.IUserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "eureka.client.registerWithEureka=false",
        "eureka.client.fetchRegistry=false"
})
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private IUserService userService;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;
    @Test
    void testDoesUsernameExist_UsernameExists() throws Exception {
        String existingUsername = "existingUser";
        Mockito.when(userService.existsUserByUsername(existingUsername)).thenReturn(true);

        mockMvc.perform(get("/api/v1/db/users/exists/username/{username}", existingUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testDoesUsernameExist_UsernameDoesNotExist() throws Exception {
        String nonExistingUsername = "nonExistingUser";
        Mockito.when(userService.existsUserByUsername(nonExistingUsername)).thenReturn(false);

        mockMvc.perform(get("/api/v1/db/users/exists/username/{username}", nonExistingUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testGetUserFromUsername_UserFound() throws Exception {
        String existingUsername = "existingUser";
        UserDTO userDTO = new UserDTO(1L, existingUsername, "password");
        Mockito.when(userService.getUserFromUsername(existingUsername)).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/api/v1/db/users/username/{username}", existingUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(userDTO)));
    }

    @Test
    void testGetUserFromUsername_UserNotFound() throws Exception {
        String nonExistingUsername = "nonExistingUser";
        Mockito.when(userService.getUserFromUsername(nonExistingUsername)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/db/users/username/{username}", nonExistingUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveUser_SuccessfulRegistration() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO("newUser", "P@ssw0rd123");

        doNothing().when(userService).saveUser(any(RegisterDTO.class));

        mockMvc.perform(post("/api/v1/db/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isCreated());

        ArgumentCaptor<RegisterDTO> registerDTOArgumentCaptor = ArgumentCaptor.forClass(RegisterDTO.class);
        verify(userService, times(1)).saveUser(registerDTOArgumentCaptor.capture());

        RegisterDTO capturedRegisterDTO = registerDTOArgumentCaptor.getValue();

        assertEquals("newUser", capturedRegisterDTO.getUsername());
        assertEquals("P@ssw0rd123", capturedRegisterDTO.getPassword());
    }

    @Test
    void testSaveUser_InvalidData() throws Exception {
        RegisterDTO invalidRegisterDTO = new RegisterDTO("", "");

        mockMvc.perform(post("/api/v1/db/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidRegisterDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).saveUser(any(RegisterDTO.class));
    }
}
