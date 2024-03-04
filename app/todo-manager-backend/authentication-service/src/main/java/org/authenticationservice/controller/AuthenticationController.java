package org.authenticationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.authenticationservice.clients.IDatabaseServiceClient;
import org.authenticationservice.constants.ErrorMessage;
import org.authenticationservice.exceptions.PasswordNotSecureException;
import org.authenticationservice.exceptions.UsernameExistsException;
import org.authenticationservice.payload.LoginDTO;
import org.authenticationservice.payload.RegisterDTO;
import org.authenticationservice.payload.TokenDTO;
import org.authenticationservice.services.IPasswordService;
import org.authenticationservice.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "the Authentication API")
public class AuthenticationController {
    private final IDatabaseServiceClient databaseServiceClient;
    private final IPasswordService passwordService;
    private final IUserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user in the system", tags = { "Authentication" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "User registration failed due to input validation errors"),
            @ApiResponse(responseCode = "409", description = "Username already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> register(@RequestBody @Valid @Parameter(description = "Registration information") RegisterDTO registerDTO) {
        if(!passwordService.checkPasswordSecurity(registerDTO.getPassword()))
            throw new PasswordNotSecureException(ErrorMessage.PASSWORD_CONSTRAINTS);

        if(databaseServiceClient.existsUsernameInDatabase(registerDTO.getUsername()))
            throw new UsernameExistsException(ErrorMessage.USERNAME_EXISTS_ALREADY);

        registerDTO.setPassword(userService.encodePassword(registerDTO.getPassword())); // encode password
        return databaseServiceClient.saveUser(registerDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Logs in a user and returns a token", tags = { "Authentication" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid credentials")
    })
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid @Parameter(description = "Login information") LoginDTO loginDTO) {
        return userService.authenticateUser(loginDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
