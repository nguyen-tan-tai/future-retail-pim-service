package com.futureretail.config;

import com.futureretail.security.JwtGrpcInterceptor;
import io.grpc.ServerInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GrpcServerConfig {

    private final JwtGrpcInterceptor jwtGrpcInterceptor;

    @Bean
    @ConditionalOnBean(JwtGrpcInterceptor.class)
    ServerInterceptor jwtServerInterceptor() {
        log.info("Registering JWT gRPC ServerInterceptor");
        return jwtGrpcInterceptor;
    }
}
