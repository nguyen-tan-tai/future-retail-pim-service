package com.futureretail.security;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtGrpcInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY =
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<String> USER_CONTEXT_KEY = Context.key("user");

    private final JwtValidator jwtValidator;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String authorizationHeader = headers.get(AUTHORIZATION_METADATA_KEY);

        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            log.warn("Missing authorization header for method: {}", call.getMethodDescriptor().getFullMethodName());
            call.close(
                    Status.UNAUTHENTICATED.withDescription("Authorization header is missing"),
                    new Metadata());
            return new ServerCall.Listener<ReqT>() {};
        }

        Optional<String> username = jwtValidator.getUsernameFromToken(authorizationHeader);

        if (username.isEmpty()) {
            log.warn("Invalid JWT token for method: {}", call.getMethodDescriptor().getFullMethodName());
            call.close(
                    Status.UNAUTHENTICATED.withDescription("Invalid or expired JWT token"),
                    new Metadata());
            return new ServerCall.Listener<ReqT>() {};
        }

        log.debug("JWT validated successfully for user: {} on method: {}",
                username.get(),
                call.getMethodDescriptor().getFullMethodName());

        // Create a new context with the username
        Context ctx = Context.current().withValue(USER_CONTEXT_KEY, username.get());
        return Contexts.interceptCall(ctx, call, headers, next);
    }
}
