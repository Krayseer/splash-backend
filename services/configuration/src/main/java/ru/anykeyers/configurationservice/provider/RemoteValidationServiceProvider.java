package ru.anykeyers.configurationservice.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Провайдер удаленного сервиса проверки ИНН
 */
@Component
public class RemoteValidationServiceProvider implements RemoteProvider, ApiKeyProvider {

    @Value("${SERVICE_TIN_API_KEY}")
    private String apiKey;

    @Value("${SERVICE_TIN_URL}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

}
