package ru.anykeyers.commonsapi.utils;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.domain.user.UserInfo;

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * Утилиты для работы с JWT
 */
public class JwtUtils {

    /**
     * Получение пользователя из {@link Jwt}
     */
    public static User extractUser(Jwt jwt) {
        return User.builder()
                .id(UUID.fromString((String) jwt.getClaims().get("sub")))
                .username((String) jwt.getClaims().get("preferred_username"))
                .userInfo(extractUserInfo(jwt))
                .setting(extractSetting(jwt))
                .build();
    }

    /**
     * Получение пользователя из {@link Principal}
     */
    public static User extractUser(Principal principal) {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) principal;
        return extractUser(jwtToken.getToken());
    }

    private static UserInfo extractUserInfo(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        return UserInfo.builder()
                .firstName((String) claims.get("given_name"))
                .lastName((String) claims.get("family_name"))
                .email((String) claims.get("email"))
                .phoneNumber((String) claims.get("phone_number"))
                .photoUrl((String) claims.get("photo_url"))
                .roles(Arrays.stream(((String) claims.get("scope")).split(" ")).toList())
                .build();
    }

    private static User.Setting extractSetting(Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        return User.Setting.builder()
                .emailEnabled((Boolean) claims.get("email_enabled"))
                .pushEnabled((Boolean) claims.get("push_enabled"))
                .build();
    }

}
