package ru.anykeyers.user.service.keycloak;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.anykeyers.user.config.KeycloakConfig;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.domain.user.UserInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Маппер для пользователя Keycloak
 */
@Component
@RequiredArgsConstructor
class KeycloakUserMapper {

    private static final String PHONE_NUMBER_ATTRIBUTE = "phone_number";

    private static final String PHOTO_URL_ATTRIBUTE = "photo_url";

    private final ModelMapper modelMapper;

    private final KeycloakConfig.Configurator keycloakConfigurator;

    /**
     * Преобразовать пользователя Keycloak в обычного пользователя
     */
    public User toUser(UserRepresentation userRepresentation) {
        User user = modelMapper.map(userRepresentation, User.class);
        user.setId(UUID.fromString(userRepresentation.getId()));
        UserInfo userInfo = modelMapper.map(userRepresentation, UserInfo.class);
        fillUserInfo(userInfo, userRepresentation.getAttributes());
        user.setUserInfo(userInfo);
        return user;
    }

    /**
     * Создать пользователя Keycloak
     */
    public UserRepresentation toKeycloakUser(User user) {
        UserRepresentation keycloakUser = modelMapper.map(user, UserRepresentation.class);
        keycloakUser.setAttributes(Map.of(
                PHONE_NUMBER_ATTRIBUTE, Collections.singletonList(user.getUserInfo().getPhoneNumber())
        ));
        keycloakUser.setCredentials(keycloakConfigurator.createPasswordCredentials(user.getPassword()));
        keycloakUser.setEnabled(true);
        return keycloakUser;
    }

    /**
     * Преобразовать поля пользователя в атрибуты
     */
    public Map<String, List<String>> toAttributes(UserInfo userInfo) {
        return Map.of(
                PHONE_NUMBER_ATTRIBUTE, Collections.singletonList(userInfo.getPhoneNumber()),
                PHOTO_URL_ATTRIBUTE, Collections.singletonList(userInfo.getPhotoUrl())
        );
    }

    private void fillUserInfo(UserInfo userInfo, Map<String, List<String>> attributes) {
        if (attributes.containsKey(PHONE_NUMBER_ATTRIBUTE)) {
            userInfo.setPhoneNumber(attributes.get(PHONE_NUMBER_ATTRIBUTE).getFirst());
        }
        if (attributes.containsKey(PHOTO_URL_ATTRIBUTE)) {
            userInfo.setPhotoUrl(attributes.get(PHOTO_URL_ATTRIBUTE).getFirst());
        }
    }

}
