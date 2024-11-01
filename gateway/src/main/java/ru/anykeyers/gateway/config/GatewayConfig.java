package ru.anykeyers.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service",
                        r -> r
                                .path("/api/user/**")
                                .uri("lb://user-service")
                )

                .route("order-service",
                        r -> r
                                .path("/api/order/**")
                                .uri("lb://order-service")
                )

                .route("configuration-service",
                        r -> r
                                .path("/api/car-wash/**")
                                .uri("lb://configuration-service")
                )

                .route("service-of-services",
                        r -> r
                                .path("/api/service/**")
                                .uri("lb://service-of-services")
                )

                .route("business-order-service",
                        r -> r
                                .path("/api/business-order/**")
                                .uri("lb://business-order-service")
                )

                .route("statistics-service",
                        r -> r
                                .path("/api/statistics/**")
                                .uri("lb://statistics-service")
                )

                .route("notification-service",
                        r -> r
                                .path("/api/notification/**")
                                .uri("lb://notification-service")
                )

                .route("storage-service",
                        r -> r
                                .path("/api/storage/**")
                                .uri("lb://storage-service")
                )

                .route("chat-service",
                        r -> r
                                .path("/api/chat/ws/**")
                                .uri("lb:ws://chat-service")
                )
                .build();
    }

}
