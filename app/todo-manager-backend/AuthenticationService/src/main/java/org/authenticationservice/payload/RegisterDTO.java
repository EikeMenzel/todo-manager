package org.authenticationservice.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.authenticationservice.constants.ErrorMessage;

@Data
@AllArgsConstructor
public class RegisterDTO {
    @NotBlank(message = ErrorMessage.USERNAME_REQUIRED)
    @Size(min = 6, max = 30, message = ErrorMessage.USERNAME_LENGTH)
    private final String username;

    @NotBlank(message = ErrorMessage.PASSWORD_REQUIRED)
    @Size(min = 8, max = 72, message = ErrorMessage.PASSWORD_LENGTH)
    private String password;
}
