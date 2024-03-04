package org.databaseservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.databaseservice.payload.RegisterDTO;
import org.databaseservice.payload.UserDTO;
import org.databaseservice.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/db/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Management", description = "API for managing user information")
public class UserController {
    private final IUserService userService;
    @GetMapping("/exists/username/{username}")
    @Operation(summary = "Check username existence", description = "Checks if a given username already exists in the system", tags = { "User Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully checked the username existence"),
            @ApiResponse(responseCode = "400", description = "Invalid username provided")
    })
    public boolean doesUsernameExist(@PathVariable @NotBlank @Parameter(description = "Username to be checked", required = true) String username) {
        return userService.existsUserByUsername(username);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieves user information based on the provided username", tags = { "User Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and information returned"),
            @ApiResponse(responseCode = "400", description = "Invalid username provided"),
            @ApiResponse(responseCode = "404", description = "User not found with the provided username")
    })
    public ResponseEntity<UserDTO> getUserFromUsername(@PathVariable @NotBlank @Parameter(description = "Username based on which user information is to be retrieved", required = true) String username) {
        return userService.getUserFromUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user", description = "Creates a new user in the system with the provided user information", tags = { "User Management" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user information provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void saveUser(@RequestBody @Valid @Parameter(description = "User information for creating a new user", required = true) RegisterDTO registerDTO) {
        userService.saveUser(registerDTO);
    }
}
