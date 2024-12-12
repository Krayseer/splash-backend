package ru.anykeyers.user.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.anykeyers.commonsapi.config.RemoteConfig;
import ru.anykeyers.commonsapi.config.WebConfig;
import ru.krayseer.storageclient.service.FileStorageClient;
import ru.krayseer.storageclient.StorageProvider;
import ru.krayseer.storageclient.service.StorageClient;

/**
 * Конфигурация приложения
 */
@Configuration
@EnableDiscoveryClient
@Import({ WebConfig.class, RemoteConfig.class })
public class ApplicationConfig {

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
    public StorageClient fileStorageClient(StorageProvider storageProvider) {
        return new FileStorageClient(storageProvider);
    }

}
