package org.gatewayservice.filters;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gatewayservice.services.JwtService;
import org.gatewayservice.services.RouterValidatorService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter implements GatewayFilter {
    private final RouterValidatorService routerValidatorService;
    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        log.info(String.valueOf(routerValidatorService.isSecured.test(request)));
        if (routerValidatorService.isSecured.test(request)) {
            if (iSAuthMissing(request))
                return errorResponse(exchange, HttpStatus.UNAUTHORIZED);

            String token = extractBearerFromHeader(request);

            if (jwtService.isTokenExpired(token)) {
                return errorResponse(exchange, HttpStatus.FORBIDDEN);
            }

            mutateRequest(exchange, token);
        }
        return chain.filter(exchange);
    }

    private boolean iSAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private Mono<Void> errorResponse(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String extractBearerFromHeader(ServerHttpRequest serverHttpRequest) {
        List<String> authorizationHeaders = serverHttpRequest.getHeaders().getOrEmpty("Authorization");
        if (!authorizationHeaders.isEmpty()) {
            String authorizationHeader = authorizationHeaders.get(0);
            if (authorizationHeader.startsWith("Bearer ")) {
                return authorizationHeader.substring(7); // Skip "Bearer " prefix
            }
        }
        return null;
    }


    private void mutateRequest(ServerWebExchange serverWebExchange, String token) {
        Optional<Claims> claims = jwtService.getAllClaimsFromToken(token);
        claims.ifPresent(value -> serverWebExchange
                .getRequest()
                .mutate()
                .header("userId", String.valueOf(value.getSubject()))
                .build());
    }
}
