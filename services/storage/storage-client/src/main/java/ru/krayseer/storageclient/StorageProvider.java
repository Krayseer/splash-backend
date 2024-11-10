package ru.krayseer.storageclient;

import io.grpc.ManagedChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Провайдер хранилища
 */
@Getter
@AllArgsConstructor
public class StorageProvider {

    /**
     * Канал для подключения к серверу хранилища
     */
    private final ManagedChannel channel;

    /**
     * Базовый URL для обращения к серверу хранилища
     */
    private final String baseUrl;

}
