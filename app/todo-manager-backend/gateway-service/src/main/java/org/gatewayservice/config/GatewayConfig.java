package org.gatewayservice.config;

import lombok.RequiredArgsConstructor;
import org.gatewayservice.filters.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {
    private final AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authentication-service",
                        r -> r.path(
                                        "/api/v1/login",
                                        "/api/v1/register"
                                )
                                .filters(f -> f.filter(authenticationFilter))
                                .uri("lb://authentication-service")
                )

                .route("task-service",
                        r -> r.path(
                                        "/api/v1/categories",
                                        "/api/v1/categories/{categoryId}",
                                        "/api/v1/categories/{categoryId}/tasks",
                                        "/api/v1/categories/{categoryId}/tasks/{toDoId}"
                                )
                                .filters(f -> f.filter(authenticationFilter))
                                .uri("lb://task-service")
                )
                .build();
    }
}
