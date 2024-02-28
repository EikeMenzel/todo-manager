package org.gatewayservice.services;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterValidatorService {
    public static final List<String> publicApiEndpoints = List.of(
            "/api/v1/register",
            "/api/v1/login"
    );

    public Predicate<ServerHttpRequest> isSecured =
            serverHttpRequest -> publicApiEndpoints
                    .stream()
                    .noneMatch(uri ->
                            serverHttpRequest.getURI()
                                    .getPath()
                                    .contains(uri)
                    );
}
