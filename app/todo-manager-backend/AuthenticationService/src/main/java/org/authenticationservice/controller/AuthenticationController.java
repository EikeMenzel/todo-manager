package org.authenticationservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.authenticationservice.clients.IDatabaseServiceClient;
import org.authenticationservice.constants.ErrorMessage;
import org.authenticationservice.exceptions.PasswordNotSecureException;
import org.authenticationservice.exceptions.UsernameExistsException;
import org.authenticationservice.payload.RegisterDTO;
import org.authenticationservice.services.IPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final IDatabaseServiceClient databaseServiceClient;
    private final IPasswordService passwordService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO registerDTO) {
        if(!passwordService.checkPasswordSecurity(registerDTO.getPassword()))
            throw new PasswordNotSecureException(ErrorMessage.PASSWORD_CONSTRAINTS);

        if(databaseServiceClient.existsUsernameInDatabase(registerDTO.getUsername()))
            throw new UsernameExistsException(ErrorMessage.USERNAME_EXISTS_ALREADY);

        return databaseServiceClient.saveUser(registerDTO);
    }
}
