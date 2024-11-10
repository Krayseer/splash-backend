package ru.anykeyers.storageservice.web;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anykeyers.storageproto.FileType;
import ru.anykeyers.storageservice.service.FileStorageService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.BASE_URL)
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/photo/{id}")
    public ResponseEntity<Resource> loadPhoto(@PathVariable UUID id) {
        return loadFile(id, FileType.PHOTO);
    }
    @GetMapping("/video/{id}")
    public ResponseEntity<Resource> loadVideo(@PathVariable UUID id) {
        return loadFile(id, FileType.VIDEO);
    }

    @SneakyThrows
    private ResponseEntity<Resource> loadFile(UUID id, FileType fileType) {
        Resource file = fileStorageService.loadFile(id, fileType);
        return ResponseEntity.ok()
                .contentLength(file.contentLength())
                .contentType(MediaTypeFactory.getMediaType(file).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(file);
    }

}
