package ru.krayseer.storageclient;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;
import ru.anykeyers.storageproto.FileType;
import ru.krayseer.storageproto.proto.FileChunk;
import ru.krayseer.storageproto.proto.FileStorageServiceGrpc;
import ru.krayseer.storageproto.proto.UploadStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        List<String> fileIds = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(files.size());
        files.forEach(file ->
                threadPool.submit(() -> {
                    try {
                        uploadFile(file.getInputStream(), FileType.PHOTO, asyncStub.uploadFile(new StreamObserver<>() {
                            @Override
                            public void onNext(UploadStatus status) {
                                fileIds.add(status.getFileId());
                            }

                            @Override
                            public void onError(Throwable t) {
                                latch.countDown();
                                throw new RuntimeException(t);
                            }

                            @Override
                            public void onCompleted() {
                                latch.countDown();
                            }
                        }));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
        latch.await();
        callbackFileIds.accept(fileIds);
    }

    @SneakyThrows
    public void uploadPhoto(MultipartFile file, Consumer<String> callbackFileId) {
        uploadFile(file.getInputStream(), FileType.PHOTO, getFileObserver(callbackFileId));
    }

    @SneakyThrows
    public void uploadVideo(MultipartFile file, Consumer<String> callbackFileId) {
        uploadFile(file.getInputStream(), FileType.VIDEO, getFileObserver(callbackFileId));
    }

    @SneakyThrows
    private void uploadFile(InputStream fileStream, FileType fileType, StreamObserver<FileChunk> observer) {
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileStream.read(buffer)) != -1) {
            FileChunk chunk = FileChunk.newBuilder()
                    .setContent(ByteString.copyFrom(buffer, 0, bytesRead))
                    .setFormat(fileType.name())
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
