package ru.anykeyers.statistics.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import ru.anykeyers.commonsapi.config.RemoteConfig;
import ru.anykeyers.commonsapi.remote.RemoteServicesService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Контекст приложения
 */
@Configuration
@EnableScheduling
@Import({ RemoteConfig.class })
public class ApplicationContext {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

//    @Bean
//    @ConfigurationProperties(prefix = "remote-configuration")
//    public RemoteProvider remoteConfiguration() {
//        return new RemoteProvider();
//    }
//
//    @Bean
//    public RemoteServicesService remoteServicesService(RestTemplate restTemplate,
//                                                       RemoteProvider remoteProvider) {
//        return new RemoteServicesService(restTemplate, remoteProvider);
//    }

}
