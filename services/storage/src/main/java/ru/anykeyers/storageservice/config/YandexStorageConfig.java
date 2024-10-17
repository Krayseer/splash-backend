package ru.anykeyers.storageservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Конфигурация Yandex Cloud хранилища
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "yandex-storage-config")
public class YandexStorageConfig {

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
