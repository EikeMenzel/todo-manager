package org.authenticationservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.authenticationservice.constants.ErrorMessage;

@Data
@AllArgsConstructor
@Schema(description = "Registration information")
public class RegisterDTO {
    @NotBlank(message = ErrorMessage.USERNAME_REQUIRED)
    @Size(min = 6, max = 30, message = ErrorMessage.USERNAME_LENGTH)
    @Schema(description = "Username for the new user", example = "john_doe")
    private final String username;

    @NotBlank(message = ErrorMessage.PASSWORD_REQUIRED)
    @Size(min = 8, max = 72, message = ErrorMessage.PASSWORD_LENGTH)
    @Schema(description = "Password for the new user", example = "P@ssw0rd")
    private String password;
}
