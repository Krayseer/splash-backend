package ru.anykeyers.configurationservice.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.anykeyers.commonsapi.config.KafkaConfig;
import ru.anykeyers.commonsapi.config.RemoteConfig;
import ru.anykeyers.commonsapi.config.WebConfig;
import ru.anykeyers.commonsapi.task.TaskService;
import ru.krayseer.storageclient.service.FileStorageClient;
import ru.krayseer.storageclient.StorageProvider;

/**
 * Контекст приложения
 */
@Configuration
@EnableScheduling
@Import({ WebConfig.class, RemoteConfig.class, KafkaConfig.class })
public class ApplicationConfig {

    @Bean
    public TaskService taskService() {
        return new TaskService();
    }

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
    public FileStorageClient fileStorageClient(TaskService taskService,
                                               StorageProvider storageProvider) {
        FileStorageClient fileStorageClient = new FileStorageClient(storageProvider);
        fileStorageClient.setTaskService(taskService);
        return fileStorageClient;
    }

}
