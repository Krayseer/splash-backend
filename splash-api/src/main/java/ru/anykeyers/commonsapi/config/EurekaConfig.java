package ru.anykeyers.commonsapi.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
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

    @PostConstruct
    public void configureEurekaInstance() {
        // Переопределение настройки клиентов для использования их через IP адрес, а не hostname
        eurekaInstanceConfigBean.setPreferIpAddress(true);
    }

}
