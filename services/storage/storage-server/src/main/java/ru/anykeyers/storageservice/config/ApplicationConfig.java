package ru.anykeyers.storageservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
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
        @Value("${BUCKET_NAME}")
        private String bucketName;
        /**
         * Ключ доступа
         */
        @Value("${ACCESS_KEY_ID}")
        private String accessKeyId;
        /**
         * Секретный ключ
         */
        @Value("${SECRET_KEY}")
        private String secretAccessKey;

    }


}
