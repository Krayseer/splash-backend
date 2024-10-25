package ru.anykeyers.user.service.keycloak;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.anykeyers.user.config.KeycloakConfig;
import ru.anykeyers.user.exception.UserNotFoundException;
import ru.anykeyers.user.exception.UserAlreadyExistsException;
import ru.anykeyers.user.service.UserService;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.domain.user.UserInfo;
import ru.krayseer.storageclient.FileStorageClient;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakUserService implements UserService {

    private final KeycloakUserMapper keycloakUserMapper;

    private final KeycloakConfig.Configurator keycloakConfigurator;

    private final FileStorageClient fileStorageClient;

    @Override
    public User getUser(UUID id) {
        UserRepresentation keycloakUser = getUserResource(id).toRepresentation();
        return keycloakUserMapper.toUser(keycloakUser);
    }

    @Override
    public User getUser(String username) {
        UserRepresentation keycloakUser = getUsersResource().search(username).stream()
                .filter(x -> x.getUsername().equals(username))
                .findAny()
                .orElseThrow(() -> new UserNotFoundException(username));
        return keycloakUserMapper.toUser(keycloakUser);
    }

    @Override
    public void addUser(User user) {
        UserRepresentation keycloakUser = keycloakUserMapper.toKeycloakUser(user);
        Response response = getUsersResource().create(keycloakUser);
        if (HttpStatus.valueOf(response.getStatus()) == HttpStatus.CONFLICT) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
    }

    @Override
    public void updateUser(User user) {
        UserResource userResource = getUserResource(user.getId());
        UserRepresentation keycloakUser = userResource.toRepresentation();
        keycloakUser.setCredentials(keycloakConfigurator.createPasswordCredentials(user.getPassword()));
        keycloakUser.setFirstName(user.getUserInfo().getFirstName());
        keycloakUser.setLastName(user.getUserInfo().getLastName());
        keycloakUser.setEmail(user.getUserInfo().getEmail());
        keycloakUser.setAttributes(keycloakUserMapper.toAttributes(user.getUserInfo()));
        userResource.update(keycloakUser);
    }

    @Override
    public void deleteUser(UUID id) {
        getUsersResource().delete(id.toString());
    }

    @Override
    public void addPhoto(User user, MultipartFile photo) {
        fileStorageClient.uploadPhoto(photo, photoId -> {
            UserInfo userInfo = user.getUserInfo();
            userInfo.setPhotoUrl(photoId);
            UserResource userResource = getUserResource(user.getId());
            UserRepresentation keycloakUser = userResource.toRepresentation();
            keycloakUser.setAttributes(keycloakUserMapper.toAttributes(userInfo));
            userResource.update(keycloakUser);
        });
    }

    private UsersResource getUsersResource() {
        return keycloakConfigurator.getUsersResource();
    }

    private UserResource getUserResource(UUID id) {
        UserResource userResource = getUsersResource().get(id.toString());
        try {
            userResource.toRepresentation();
        } catch (NotFoundException exception) {
            throw new UserNotFoundException(id);
        }
        return userResource;
    }

}
