package org.authenticationservice.clients;

import org.authenticationservice.payload.RegisterDTO;
import org.authenticationservice.payload.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "database-service")
@SuppressWarnings("java:S1075")
public interface IDatabaseServiceClient {
    @Value("${database.base.api.path}")
    String BASE_PATH = "/api/v1/db";

    /**
     * Checks if a username exists in the database.
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    @GetMapping(BASE_PATH + "/users/exists/username/{username}")
    boolean existsUsernameInDatabase(@PathVariable("username") String username);

    @PostMapping(BASE_PATH + "/users")
    ResponseEntity<Void> saveUser(@RequestBody RegisterDTO registerDTO);

    @GetMapping(BASE_PATH + "/users/username/{username}")
    ResponseEntity<UserDTO> getUserDTOFromUsername(@PathVariable("username") String username);
}
