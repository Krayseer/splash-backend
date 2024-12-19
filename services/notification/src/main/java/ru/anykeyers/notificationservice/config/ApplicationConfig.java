package ru.anykeyers.notificationservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.anykeyers.commonsapi.config.EurekaConfig;
import ru.anykeyers.commonsapi.config.KafkaConfig;
import ru.anykeyers.commonsapi.config.RemoteConfig;
import ru.anykeyers.commonsapi.config.WebConfig;

/**
 * Конфигурация приложения приложения
 */
@Configuration
@Import({ WebConfig.class, RemoteConfig.class, KafkaConfig.class, EurekaConfig.class })
public class ApplicationConfig {
}
