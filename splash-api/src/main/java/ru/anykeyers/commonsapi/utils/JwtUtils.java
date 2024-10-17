package ru.anykeyers.commonsapi.utils;

import org.springframework.security.oauth2.jwt.Jwt;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.domain.user.UserInfo;

import java.util.UUID;

/**
 * Утилиты для работы с JWT
 */
public class JwtUtils {

    /**
     * Получение пользователя из JWT
     */
    public static User extractUser(Jwt jwt) {
        return User.builder()
                .id(UUID.fromString((String) jwt.getClaims().get("sub")))
                .username((String) jwt.getClaims().get("preferred_username"))
                .userInfo(extractUserInfo(jwt))
                .build();
    }

    private static UserInfo extractUserInfo(Jwt jwt) {
        return UserInfo.builder()
                .firstName((String) jwt.getClaims().get("name"))
                .photoUrl((String) jwt.getClaims().get("photo_url"))
                .build();
    }

}
