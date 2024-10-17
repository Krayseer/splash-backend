package ru.anykeyers.orderservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.anykeyers.commonsapi.config.RemoteConfig;
import ru.anykeyers.commonsapi.config.WebConfig;

/**
 * Контекст приложения
 */
@Configuration
@EnableScheduling
@Import({ WebConfig.class, RemoteConfig.class })
public class ApplicationConfig {
}
