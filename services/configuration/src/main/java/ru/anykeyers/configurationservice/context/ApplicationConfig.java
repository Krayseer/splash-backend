package ru.anykeyers.configurationservice.context;

import io.grpc.ManagedChannelBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.anykeyers.commonsapi.config.RemoteConfig;
import ru.anykeyers.commonsapi.config.WebConfig;
import ru.krayseer.storageclient.FileStorageClient;

/**
 * Контекст приложения
 */
@Configuration
@EnableDiscoveryClient
@EnableFeignClients
@Import({ WebConfig.class, RemoteConfig.class })
public class ApplicationConfig {

    @Bean
    public FileStorageClient fileStorageClient() {
        return new FileStorageClient(
                ManagedChannelBuilder
                        .forAddress("localhost", 9090)
                        .usePlaintext()
                        .build()
        );
    }


}
