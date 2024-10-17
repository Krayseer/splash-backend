package ru.anykeyers.commonsapi.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация по работе с удаленными сервисами
 */
@Configuration
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "ru.anykeyers.commonsapi.remote")
public class RemoteConfig {
}
