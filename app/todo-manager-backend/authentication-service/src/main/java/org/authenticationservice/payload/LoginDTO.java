package org.authenticationservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.authenticationservice.constants.ErrorMessage;

@Data
@AllArgsConstructor
public class LoginDTO {
    @NotBlank(message = ErrorMessage.USERNAME_REQUIRED)
    @Schema(description = "Username of the user", example = "john_doe")
    private final String username;

    @NotBlank(message = ErrorMessage.PASSWORD_REQUIRED)
    @Schema(description = "Password of the user", example = "P@ssw0rd")
    private String password;
}
