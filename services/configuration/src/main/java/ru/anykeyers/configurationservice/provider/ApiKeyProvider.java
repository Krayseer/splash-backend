package ru.anykeyers.configurationservice.provider;

/**
 * Провайдер, поддерживающий отправку запросов через API ключ
 */
public interface ApiKeyProvider {

    /**
     * @return API ключ для отправки запроса
     */
    String getApiKey();

}
