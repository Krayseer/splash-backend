package ru.anykeyers.commonsapi.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.krayseer.storageclient.FileStorageClient;
import ru.krayseer.storageclient.StorageProvider;

/**
 * Конфигурация хранилища
 */
@Configuration
public class StorageConfig {

    @Value("${GATEWAY_HOST}")
    private String gatewayHost;

    @Value("${GATEWAY_PORT}")
    private String gatewayPort;

    @Value("${STORAGE_SERVICE_HOST}")
    private String storageServiceHost;

    @Value("${STORAGE_SERVICE_PORT}")
    private int storageServicePort;

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress(storageServiceHost, storageServicePort)
                .usePlaintext()
                .build();
    }

    @Bean
    public StorageProvider storageProvider(ManagedChannel managedChannel) {
        String gatewayUrl = gatewayHost + ":" + gatewayPort;
        return new StorageProvider(managedChannel, gatewayUrl + "/api/storage");
    }

    @Bean
    public FileStorageClient fileStorageClient(StorageProvider storageProvider) {
        return new FileStorageClient(storageProvider);
    }

}
