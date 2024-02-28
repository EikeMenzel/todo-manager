package org.databaseservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.payload.RegisterDTO;
import org.databaseservice.payload.UserDTO;
import org.databaseservice.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/api/v1/db/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    @GetMapping("/exists/username/{username}")
    public boolean doesUsernameExist(@PathVariable String username) {
        return userService.existsUserByUsername(username);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserFromUsername(@PathVariable String username) {
        return userService.getUserFromUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public void saveUser(@RequestBody() @Valid RegisterDTO registerDTO) {
        userService.saveUser(registerDTO);
    }
}
