package ru.anykeyers.notificationservice.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.anykeyers.commonsapi.config.KafkaConfig;
import ru.anykeyers.commonsapi.config.RemoteConfig;
import ru.anykeyers.commonsapi.config.WebConfig;

/**
 * Конфигурация приложения приложения
 */
@Configuration
@EnableDiscoveryClient
@Import({ WebConfig.class, RemoteConfig.class, KafkaConfig.class })
public class ApplicationConfig {
}