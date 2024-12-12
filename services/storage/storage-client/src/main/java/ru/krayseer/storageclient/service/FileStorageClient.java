package ru.krayseer.storageclient.service;

import io.grpc.stub.StreamObserver;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import ru.anykeyers.commonsapi.task.TaskService;
import ru.anykeyers.storageproto.FileType;
import ru.krayseer.storageclient.StorageProvider;
import ru.krayseer.storageclient.domain.UploadFileTask;
import ru.krayseer.storageproto.proto.FileChunk;
import ru.krayseer.storageproto.proto.FileStorageServiceGrpc;
import ru.krayseer.storageproto.proto.UploadStatus;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Клиент для работы с удаленным хранилищем
 */
public class FileStorageClient implements StorageClient {

    private final StorageProvider storageProvider;

    private final FileStorageServiceGrpc.FileStorageServiceStub asyncStub;

    @Setter
    private TaskService taskService;

    public FileStorageClient(StorageProvider storageProvider) {
        this.storageProvider = storageProvider;
        this.asyncStub = FileStorageServiceGrpc.newStub(storageProvider.getChannel());
    }

    @Override
    public void uploadPhoto(MultipartFile file, Consumer<String> callbackFileId, UUID userId) {
        ensureTaskServiceInitialized();
        taskService.addTask(new UploadFileTask(
                file, FileType.PHOTO, userId, getStreamObserverCreator(callbackFileId, FileType.PHOTO)
        ));
    }

    @Override
    public void uploadVideo(MultipartFile file, Consumer<String> callbackFileId, UUID userId) {
        ensureTaskServiceInitialized();
        taskService.addTask(new UploadFileTask(
                file, FileType.VIDEO, userId, getStreamObserverCreator(callbackFileId, FileType.VIDEO)
        ));
    }

    private Supplier<StreamObserver<FileChunk>> getStreamObserverCreator(Consumer<String> callbackFileId, FileType fileType) {
        return () -> getFileObserver(callbackFileId, fileType);
    }

    private StreamObserver<FileChunk> getFileObserver(Consumer<String> callbackFileId, FileType fileType) {
        return asyncStub.uploadFile(createStreamObserver(callbackFileId, fileType));
    }

    private StreamObserver<UploadStatus> createStreamObserver(Consumer<String> callbackFileId, FileType fileType) {
        return new StreamObserver<>() {
            @Override
            public void onNext(UploadStatus status) {
                String photoUrl = String.format(
                        "%s/%s/%s", storageProvider.getBaseUrl(), fileType.name().toLowerCase(), status.getFileId()
                );
                callbackFileId.accept(photoUrl);
            }

            @Override
            public void onError(Throwable t) {
                throw new RuntimeException(t);
            }

            @Override
            public void onCompleted() {
            }
        };
    }

    /**
     * Проверяет, инициализирован ли taskService, иначе выбрасывает исключение.
     */
    private void ensureTaskServiceInitialized() {
        if (taskService == null) {
            taskService = new TaskService();
        }
    }

}
