package ru.anykeyers.commonsapi.config;

import com.netflix.discovery.DefaultEurekaClientConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация всех Eureka клиентов
 */
@Configuration
@EnableDiscoveryClient
public class EurekaConfig {

    @Autowired(required = false)
    private EurekaInstanceConfigBean eurekaInstanceConfigBean;

    @Autowired(required = false)
    private EurekaClientConfigBean eurekaClientConfigBean;

    @Value("${DISCOVERY_HOST}")
    private String discoveryHost;

    @Value("${DISCOVERY_PORT}")
    private String discoveryPort;

    @PostConstruct
    public void configureEurekaInstance() {
        // Переопределение настройки клиентов для использования их через IP адрес, а не hostname
        eurekaInstanceConfigBean.setPreferIpAddress(true);

        // Переопределение зоны нахождения registry-service
        String zone = String.format("http://%s:%s/eureka", discoveryHost, discoveryPort);
        eurekaClientConfigBean.getServiceUrl().put(DefaultEurekaClientConfig.DEFAULT_ZONE, zone);
    }

}
