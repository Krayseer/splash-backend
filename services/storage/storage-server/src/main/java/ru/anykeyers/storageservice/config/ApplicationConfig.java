package ru.anykeyers.storageservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationConfig {

    /**
     * Путь до директории, где сохраняются файлы
     */
    private String filesPath;

    /**
     * Конфигурация яндекс хранилища
     */
    private YandexStorageConfig yandexStorageConfig;

    @Getter
    @Setter
    public static class YandexStorageConfig {

        /**
         * Название бакета хранилища
         */
        private String bucketName;
        /**
         * Ключ доступа
         */
        private String accessKeyId;
        /**
         * Секретный ключ
         */
        private String secretAccessKey;

    }


}
