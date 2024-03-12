package org.gatewayservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouterValidatorServiceTest {

    private RouterValidatorService routerValidatorService;

    @BeforeEach
    void setUp() {
        routerValidatorService = new RouterValidatorService();
    }

    @Test
    void isSecured_ForPublicEndpoint_ShouldReturnFalse() {
        for (String publicEndpoint : RouterValidatorService.publicApiEndpoints) {
            ServerHttpRequest request = MockServerHttpRequest.get(publicEndpoint).build();
            Predicate<ServerHttpRequest> isSecured = routerValidatorService.isSecured;
            assertFalse(isSecured.test(request), "Request to " + publicEndpoint + " should not be secured.");
        }
    }

    @Test
    void isSecured_ForNonPublicEndpoint_ShouldReturnTrue() {
        String[] nonPublicEndpoints = {
                "/api/v1/user/profile",
                "/api/v1/user/settings"
        };

        for (String nonPublicEndpoint : nonPublicEndpoints) {
            ServerHttpRequest request = MockServerHttpRequest.get(nonPublicEndpoint).build();
            Predicate<ServerHttpRequest> isSecured = routerValidatorService.isSecured;
            assertTrue(isSecured.test(request), "Request to " + nonPublicEndpoint + " should be secured.");
        }
    }
}
