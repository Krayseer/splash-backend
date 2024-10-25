package ru.krayseer.storageclient;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;
import ru.krayseer.storageproto.proto.FileChunk;
import ru.krayseer.storageproto.proto.FileStorageServiceGrpc;
import ru.krayseer.storageproto.proto.UploadStatus;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * Клиент для работы с удаленным хранилищем
 */
public class FileStorageClient {

    private final FileStorageServiceGrpc.FileStorageServiceStub asyncStub;

    public FileStorageClient(ManagedChannel channel) {
        asyncStub = FileStorageServiceGrpc.newStub(channel);
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
                    .setFormat("test")
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
