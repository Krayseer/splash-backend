package ru.anykeyers.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.anykeyers.commonsapi.config.RemoteConfig;
import ru.anykeyers.commonsapi.config.WebConfig;

/**
 * Конфигурация приложения
 */
@Configuration
@Import({ WebConfig.class, RemoteConfig.class })
public class ApplicationConfig {
}
