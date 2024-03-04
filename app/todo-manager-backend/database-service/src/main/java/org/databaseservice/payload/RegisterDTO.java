package org.databaseservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.databaseservice.constants.ErrorMessage;

@Data
@AllArgsConstructor
@Schema(description = "Registration Data Transfer Object")
public class RegisterDTO {
    @NotBlank(message = ErrorMessage.USERNAME_REQUIRED)
    @Size(min = 6, max = 30, message = ErrorMessage.USERNAME_LENGTH)
    @Schema(description = "Unique username for the new user", example = "john_doe")
    private final String username;

    @NotBlank(message = ErrorMessage.PASSWORD_REQUIRED)
    @Size(min = 8, max = 72, message = ErrorMessage.PASSWORD_LENGTH)
    @Schema(description = "Password for the new user account. Must be at least 8 characters long and no more than 72 characters.", example = "SecureP@ssw0rd!")
    private String password;
}
