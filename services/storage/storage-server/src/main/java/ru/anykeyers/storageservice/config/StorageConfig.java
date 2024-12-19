package ru.anykeyers.storageservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.anykeyers.commonsapi.config.EurekaConfig;

/**
 * Конфигурация хранилища
 */
@Configuration
@Import({ EurekaConfig.class })
public class StorageConfig {
}
