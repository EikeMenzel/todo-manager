package org.databaseservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private final Long id;
    private final String username;
    private final String password;
}
