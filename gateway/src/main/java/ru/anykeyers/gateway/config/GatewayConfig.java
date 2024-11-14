package ru.anykeyers.gateway.config;

import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public GlobalFilter globalFilter(TokenRelayGatewayFilterFactory factory) {
        return (exchange, chain) -> factory.apply().filter(exchange, chain);
    }

    @Bean
    public DiscoveryLocatorProperties locatorProperties() {
        DiscoveryLocatorProperties properties = new DiscoveryLocatorProperties();
        properties.setEnabled(true);
        properties.setLowerCaseServiceId(true);
        return properties;
    }

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

                .route("chat-service-socket",
                        r -> r
                                .path("/api/chat/ws/**")
                                .uri("lb:ws://chat-service")
                )

                .route("chat-service-rest",
                        r -> r
                                .path("/api/rest/chat/**")
                                .uri("lb://chat-service")
                )
                .build();
    }

}
