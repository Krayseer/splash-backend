package ru.anykeyers.configurationservice.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.anykeyers.commonsapi.config.KafkaConfig;
import ru.anykeyers.commonsapi.config.RemoteConfig;
import ru.anykeyers.commonsapi.config.StorageConfig;
import ru.anykeyers.commonsapi.config.WebConfig;

/**
 * Контекст приложения
 */
@Configuration
@Import({ WebConfig.class, RemoteConfig.class, KafkaConfig.class, StorageConfig.class })
public class ApplicationConfig {
}
