package ru.anykeyers.commonsapi.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.anykeyers.commonsapi.domain.user.User;

/**
 * Удаленный сервис хранилища
 */
@FeignClient(
        name = "storage-service",
        path = "/api/storage"
)
public interface RemoteStorageService {

    @GetMapping("/photo/{photoId}")
    ResponseEntity<Resource> getPhoto(@PathVariable("photoId") String photoId);

}
