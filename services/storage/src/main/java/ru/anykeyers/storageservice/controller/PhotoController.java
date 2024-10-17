package ru.anykeyers.storageservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.anykeyers.storageservice.service.PhotoService;

/**
 * REST контроллер для обработки фотографий
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.PHOTO_CONTROLLER)
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String upload(@RequestBody MultipartFile photo) {
        return photoService.upload(photo);
    }

}
