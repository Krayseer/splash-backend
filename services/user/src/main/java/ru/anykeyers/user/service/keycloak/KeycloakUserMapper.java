package ru.anykeyers.user.service.keycloak;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.anykeyers.user.config.KeycloakConfig;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.domain.user.UserInfo;

import java.util.*;

/**
 * Маппер для пользователя Keycloak
 */
@Component
@RequiredArgsConstructor
class KeycloakUserMapper {

    private static final String PHONE_NUMBER_ATTRIBUTE = "phone_number";

    private static final String PHOTO_URL_ATTRIBUTE = "photo_url";

    private static final String PUSH_ENABLED_ATTRIBUTE = "push_enabled";

    private static final String EMAIL_ENABLED_ATTRIBUTE = "email_enabled";

    private final ModelMapper modelMapper;

    private final KeycloakConfig.Configurator keycloakConfigurator;

    /**
     * Преобразовать пользователя Keycloak в обычного пользователя
     */
    public User toUser(UserRepresentation userRepresentation) {
        User user = modelMapper.map(userRepresentation, User.class);
        user.setId(UUID.fromString(userRepresentation.getId()));
        UserInfo userInfo = modelMapper.map(userRepresentation, UserInfo.class);
        fillUser(user, userRepresentation.getAttributes());
        user.setUserInfo(userInfo);
        return user;
    }

    /**
     * Создать пользователя Keycloak
     */
    public UserRepresentation toKeycloakUser(User user) {
        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(user.getUsername());
        keycloakUser.setEmail(user.getUserInfo().getEmail());
        keycloakUser.setFirstName(user.getUserInfo().getFirstName());
        keycloakUser.setLastName(user.getUserInfo().getLastName());
        keycloakUser.setAttributes(toAttributes(user));
        keycloakUser.setCredentials(keycloakConfigurator.createPasswordCredentials(user.getPassword()));
        keycloakUser.setEnabled(true);
        return keycloakUser;
    }

    /**
     * Преобразовать поля пользователя в атрибуты
     */
    public Map<String, List<String>> toAttributes(User user) {
        HashMap<String, List<String>> attrs = new HashMap<>();
        if (user.getUserInfo() != null) {
            attrs.put(PHONE_NUMBER_ATTRIBUTE, Collections.singletonList(user.getUserInfo().getPhoneNumber()));
            attrs.put(PHOTO_URL_ATTRIBUTE, Collections.singletonList(user.getUserInfo().getPhotoUrl()));
        }
        attrs.put(PUSH_ENABLED_ATTRIBUTE, Collections.singletonList(
                Boolean.toString(user.getSetting() == null || user.getSetting().isPushEnabled())
        ));
        attrs.put(EMAIL_ENABLED_ATTRIBUTE, Collections.singletonList(
                Boolean.toString(user.getSetting() == null || user.getSetting().isEmailEnabled())
        ));
        return attrs;
    }

    private void fillUser(User user, Map<String, List<String>> attributes) {
        UserInfo userInfo = new UserInfo();
        if (attributes.containsKey(PHONE_NUMBER_ATTRIBUTE)) {
            userInfo.setPhoneNumber(attributes.get(PHONE_NUMBER_ATTRIBUTE).getFirst());
        }
        if (attributes.containsKey(PHOTO_URL_ATTRIBUTE)) {
            userInfo.setPhotoUrl(attributes.get(PHOTO_URL_ATTRIBUTE).getFirst());
        }

        User.Setting setting = new User.Setting();
        if (attributes.containsKey(PUSH_ENABLED_ATTRIBUTE)) {
            setting.setPushEnabled(Boolean.parseBoolean(attributes.get(PUSH_ENABLED_ATTRIBUTE).getFirst()));
        }
        if (attributes.containsKey(EMAIL_ENABLED_ATTRIBUTE)) {
            setting.setEmailEnabled(Boolean.parseBoolean(attributes.get(EMAIL_ENABLED_ATTRIBUTE).getFirst()));
        }

        user.setUserInfo(userInfo);
        user.setSetting(setting);
    }

}
