package ru.anykeyers.configurationservice.provider;

import org.springframework.web.client.RestTemplate;

/**
 * Провайдер удаленного сервиса
 */
public interface RemoteProvider {

    /**
     * @return базовый URL сервиса
     */
    String getBaseUrl();

    /**
     * @return объект для работы с запросами по сети
     */
    RestTemplate getRestTemplate();

}
