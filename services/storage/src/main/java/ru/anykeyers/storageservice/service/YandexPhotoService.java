package ru.anykeyers.storageservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.anykeyers.storageservice.config.AmazonConfigurator;
import ru.anykeyers.storageservice.config.YandexStorageConfig;

import java.io.ByteArrayInputStream;
import java.util.UUID;

/**
 * Сервис обработки фотографий Yandex Cloud
 */
@Slf4j
@Service
@Order(1)
@Primary
public class YandexPhotoService implements PhotoService {

    private final YandexStorageConfig storageConfig;

    private final AmazonConfigurator amazonConfigurator;

    @Autowired
    public YandexPhotoService(YandexStorageConfig storageConfig,
                              AmazonConfigurator amazonConfigurator) {
        this.storageConfig = storageConfig;
        this.amazonConfigurator = amazonConfigurator;
    }

    @Override
    @SneakyThrows
    public String upload(MultipartFile photo) {
        String bucketName = storageConfig.getBucketName();
        AmazonS3 s3Client = amazonConfigurator.createAmazonS3Client(
                storageConfig.getAccessKeyId(), storageConfig.getSecretAccessKey()
        );
        String photoName = generateUniqueName();
        byte[] photoBytes = photo.getBytes();
        ByteArrayInputStream photoInputStream = new ByteArrayInputStream(photoBytes);
        s3Client.putObject(bucketName, photoName, photoInputStream, createPhotoMetadata(photo));
        return s3Client.getUrl(bucketName, photoName).toExternalForm();
    }

    @SneakyThrows
    private ObjectMetadata createPhotoMetadata(MultipartFile photo) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(photo.getBytes().length);
        return metadata;
    }

    private String generateUniqueName() {
        return UUID.randomUUID().toString();
    }

}
