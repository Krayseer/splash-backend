package ru.krayseer.storageclient.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Клиент хранилища
 */
public interface StorageClient {

    /**
     * Загрузить фотографию
     *
     * @param file              файл фотографии
     * @param callbackFileId    обработчик возвращаемого идентификатора файла
     * @param userId            идентификатор пользователя
     */
    void uploadPhoto(MultipartFile file, Consumer<String> callbackFileId, UUID userId);

    /**
     * Загрузить видеоролик
     *
     * @param file              файл видеоролика
     * @param callbackFileId    обработчик возвращаемого идентификатора файла
     * @param userId            идентификатор пользователя
     */
    void uploadVideo(MultipartFile file, Consumer<String> callbackFileId, UUID userId);

}
