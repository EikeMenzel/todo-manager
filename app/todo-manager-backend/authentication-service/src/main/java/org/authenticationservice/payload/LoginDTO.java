package org.authenticationservice.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.authenticationservice.constants.ErrorMessage;

@Data
@AllArgsConstructor
public class LoginDTO {
    @NotBlank(message = ErrorMessage.USERNAME_REQUIRED)
    private final String username;

    @NotBlank(message = ErrorMessage.PASSWORD_REQUIRED)
    private String password;
}
