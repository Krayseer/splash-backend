package ru.anykeyers.commonsapi.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * Удаленный сервис хранилища
 */
@FeignClient(
        name = "storage-service",
        path = "/api/storage"
)
public interface RemoteStorageService {
    @GetMapping
    ResponseEntity<String> uploadPhoto(MultipartFile photo);
// TODO: Это все нужно перерабатывать в целом, создавать grpc клиента и тд
//    /**
//     * Загрузить фото в хранилище
//     *
//     * @param photo фотография
//     * @return идентификатор фотографии
//     */
//    @SneakyThrows
//    public ResponseEntity<String> uploadPhoto(MultipartFile photo) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<byte[]> requestEntity = new HttpEntity<>(photo.getBytes(), headers);
//        try {
//            ResponseEntity<String> response = remoteStorageProvider.getRestTemplate().exchange(
//                    remoteStorageProvider.getBaseUrl() + "/photo", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {}
//            );
//            if (response.getStatusCode().is2xxSuccessful()) {
//                return response;
//            }
//            ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
//            if (response.hasBody()) {
//                return responseBuilder.body(response.getBody());
//            }
//            return responseBuilder.build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
//        }
//    }

}
