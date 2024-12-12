package ru.anykeyers.configurationservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.provider.RemoteValidationServiceProvider;
import ru.anykeyers.configurationservice.service.ValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    /**
     * Префикс для авторизации
     */
    private final String TOKEN_PREFIX = "Token ";

    private final RemoteValidationServiceProvider remoteProvider;

    @Override
    public boolean validateTinConfiguration(Configuration configuration) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", TOKEN_PREFIX + remoteProvider.getApiKey());
        headers.set("Content-Type", "application/json");
        Query query = new Query(configuration.getOrganizationInfo().getTin());
        HttpEntity<Query> entity = new HttpEntity<>(query, headers);
        SuggestionsResponse response = remoteProvider.getRestTemplate().postForObject(
                remoteProvider.getBaseUrl(), entity, SuggestionsResponse.class
        );
        if (response == null) {
            throw new IllegalArgumentException();
        }
        return !response.suggestions.isEmpty();
    }

    /**
     * Ответ с данными по ИНН
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SuggestionsResponse {
        private List<Object> suggestions;
    }

    /**
     * Тело запроса ИНН
     */
    @Data
    @AllArgsConstructor
    private static class Query {
        private final String query;
    }

}
