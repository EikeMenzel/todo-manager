package org.authenticationservice.controller;

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
public class AuthenticationController {
    private final IDatabaseServiceClient databaseServiceClient;
    private final IPasswordService passwordService;
    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO registerDTO) {
        if(!passwordService.checkPasswordSecurity(registerDTO.getPassword()))
            throw new PasswordNotSecureException(ErrorMessage.PASSWORD_CONSTRAINTS);

        if(databaseServiceClient.existsUsernameInDatabase(registerDTO.getUsername()))
            throw new UsernameExistsException(ErrorMessage.USERNAME_EXISTS_ALREADY);

        registerDTO.setPassword(userService.encodePassword(registerDTO.getPassword())); // encode password
        return databaseServiceClient.saveUser(registerDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        return userService.authenticateUser(loginDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
