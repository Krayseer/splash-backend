package ru.anykeyers.storageservice.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Сервис обработки фотографий
 */
public interface PhotoService {

    /**
     * Загрузить файл
     *
     * @param file файл
     * @return идентификатор файла
     */
    String upload(MultipartFile file);

}
