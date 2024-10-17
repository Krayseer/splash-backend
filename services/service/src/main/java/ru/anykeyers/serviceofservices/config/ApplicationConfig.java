package ru.anykeyers.serviceofservices.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.anykeyers.commonsapi.config.WebConfig;

/**
 * Конфигурация приложения
 */
@Configuration
@EnableDiscoveryClient
@Import(WebConfig.class)
public class ApplicationConfig {
}
