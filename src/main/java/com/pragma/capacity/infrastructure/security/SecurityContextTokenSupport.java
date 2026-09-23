package com.pragma.capacity.infrastructure.security;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

import java.util.Objects;

public final class SecurityContextTokenSupport {

    private SecurityContextTokenSupport() {
    }

    public static Mono<String> currentToken() {
        return ReactiveSecurityContextHolder.getContext()
                .mapNotNull(ctx -> (String) Objects.requireNonNull(ctx.getAuthentication()).getCredentials());
    }
}
