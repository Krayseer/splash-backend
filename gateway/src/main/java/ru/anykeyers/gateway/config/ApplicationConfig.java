package ru.anykeyers.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.anykeyers.commonsapi.config.EurekaConfig;

/**
 * Конфигурация приложения
 */
@Configuration
@Import({ EurekaConfig.class })
public class ApplicationConfig {
}
