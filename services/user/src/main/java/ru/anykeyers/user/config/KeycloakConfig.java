package ru.anykeyers.user.config;

import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Конфигурация Keycloak
 */
@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak(@Value("${KEYCLOAK_SERVER_URL}") String serverUrl,
                             @Value("${KEYCLOAK_REALM}") String realm,
                             @Value("${KEYCLOAK_CLIENT_ID}") String clientId,
                             @Value("${KEYCLOAK_CLIENT_SECRET}") String clientSecret,
                             @Value("${KEYCLOAK_USERNAME}") String username,
                             @Value("${KEYCLOAK_PASSWORD}") String password) {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .username(username)
                .password(password)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();
    }

    @Bean
    public Configurator keycloakConfigurator(Keycloak keycloak) {
        return new Configurator(keycloak);
    }

    /**
     * Конфигуратор для Keycloak
     */
    @RequiredArgsConstructor
    public static class Configurator {

        private final Keycloak keycloak;

        @Value("${KEYCLOAK_REALM}")
        private String realmName;

        /**
         * Получить ресурс пользователей
         */
        public UsersResource getUsersResource() {
            return keycloak.realm(realmName).users();
        }

        /**
         * Создать данные о пароле пользователя
         *
         * @param password пароль пользователя
         */
        public List<CredentialRepresentation> createPasswordCredentials(String password) {
            CredentialRepresentation passwordCredentials = new CredentialRepresentation();
            passwordCredentials.setTemporary(false);
            passwordCredentials.setType(CredentialRepresentation.PASSWORD);
            passwordCredentials.setValue(password);
            return Collections.singletonList(passwordCredentials);
        }

    }

}
