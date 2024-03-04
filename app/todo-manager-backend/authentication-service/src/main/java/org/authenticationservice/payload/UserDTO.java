package org.authenticationservice.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "User Data Transfer Object used internally for user verification and service layer operations.")
public class UserDTO {
    @Schema(description = "The unique identifier of the user.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private final Long id;

    @Schema(description = "The username of the user.", example = "john_doe")
    private final String username;

    @Schema(description = "The password of the user. This field is used for verification purposes and should be handled securely.", example = "P@ssw0rd!")
    private final String password;
}

