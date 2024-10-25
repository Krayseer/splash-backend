package ru.krayseer.storageclient;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;
import ru.krayseer.storageproto.proto.FileChunk;
import ru.krayseer.storageproto.proto.FileStorageServiceGrpc;
import ru.krayseer.storageproto.proto.UploadStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * Клиент для работы с удаленным хранилищем
 */
public class FileStorageClient {

    private final FileStorageServiceGrpc.FileStorageServiceStub asyncStub;

    private final ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();

    public FileStorageClient(ManagedChannel channel) {
        asyncStub = FileStorageServiceGrpc.newStub(channel);
    }

    @SneakyThrows
    public void uploadPhotos(List<MultipartFile> files, Consumer<List<String>> callbackFileIds) {
        List<Future<?>> tasks = new ArrayList<>();
        List<String> fileIds = new ArrayList<>();
        files.forEach(file -> tasks.add(threadPool.submit(() -> {
            try {
                uploadFile(file.getInputStream(), ".jpg", getFileObserver(fileIds::add));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        })));
        for(Future<?> task : tasks) {
            task.get();
        }
        callbackFileIds.accept(fileIds);
    }

    @SneakyThrows
    public void uploadPhoto(MultipartFile file, Consumer<String> callbackFileId) {
        uploadFile(file.getInputStream(), ".jpg", getFileObserver(callbackFileId));
    }

    @SneakyThrows
    public void uploadVideo(MultipartFile file, Consumer<String> callbackFileId) {
        uploadFile(file.getInputStream(), ".mp4", getFileObserver(callbackFileId));
    }

    @SneakyThrows
    private void uploadFile(InputStream fileStream, String format, StreamObserver<FileChunk> observer) {
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileStream.read(buffer)) != -1) {
            FileChunk chunk = FileChunk.newBuilder()
                    .setContent(ByteString.copyFrom(buffer, 0, bytesRead))
                    .setFormat(format)
                    .build();
            observer.onNext(chunk);
        }
        observer.onCompleted();
    }

    private StreamObserver<FileChunk> getFileObserver(Consumer<String> callbackFileId) {
        return asyncStub.uploadFile(new StreamObserver<>() {
            @Override
            public void onNext(UploadStatus status) {
                callbackFileId.accept(status.getFileId());
            }

            @Override
            public void onError(Throwable t) {
                throw new RuntimeException(t);
            }

            @Override
            public void onCompleted() {
            }
        });
    }

}
