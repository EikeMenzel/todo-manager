package org.databaseservice.controller;

import lombok.RequiredArgsConstructor;
import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.payload.RegisterDTO;
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

    @GetMapping("/{userId}")
    public String getAllUsers(@PathVariable Long userId) {
        if(userId == 1)
            return "<";
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<Void> saveUser(@RequestBody() RegisterDTO registerDTO) {
        return userService.saveUser(registerDTO)
                ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.internalServerError().build();
    }
}
