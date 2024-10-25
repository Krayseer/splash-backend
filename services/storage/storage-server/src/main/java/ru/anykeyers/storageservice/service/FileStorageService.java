package ru.anykeyers.storageservice.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import ru.anykeyers.storageservice.config.ApplicationConfig;
import ru.anykeyers.storageservice.domain.FileType;
import ru.krayseer.storageproto.proto.FileChunk;
import ru.krayseer.storageproto.proto.FileStorageServiceGrpc;
import ru.krayseer.storageproto.proto.UploadStatus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class FileStorageService extends FileStorageServiceGrpc.FileStorageServiceImplBase {

    private final ApplicationConfig appConfig;

    /**
     * Получить файл
     *
     * @param id        идентификатор файла
     * @param fileType  тип файла
     */
    public Resource loadFile(UUID id, FileType fileType) {
        File file = new File(getFilePath(id, fileType).toUri());
        return new FileSystemResource(file);
    }

    /**
     * Загрузить файл
     *
     * @param responseObserver слушатель отправки частей видеоролика
     * @return уникальный идентификатор файла
     */
    @Override
    public StreamObserver<FileChunk> uploadFile(StreamObserver<UploadStatus> responseObserver) {
        return new StreamObserver<>() {
            private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            private FileType fileType = null;

            @Override
            public void onNext(FileChunk chunk) {
                try {
                    if (fileType == null) {
                        fileType = FileType.getFileType(chunk.getFormat());
                    }
                    outputStream.write(chunk.getContent().toByteArray());
                } catch (IOException e) {
                    throw new RuntimeException("Error write file chunk");
                }
            }

            @Override
            public void onError(Throwable t) {
                throw new RuntimeException(t);
            }

            @Override
            public void onCompleted() {
                try {
                    UUID fileId = UUID.randomUUID();
                    Files.write(getFilePath(fileId, fileType), outputStream.toByteArray());
                    UploadStatus status = UploadStatus.newBuilder()
                            .setSuccess(true)
                            .setFileId(fileId.toString())
                            .build();
                    responseObserver.onNext(status);
                    responseObserver.onCompleted();
                } catch (IOException e) {
                    responseObserver.onError(e);
                }
            }
        };
    }

    private Path getFilePath(UUID id, FileType fileType) {
        String fileName = id + fileType.getFormat();
        return Paths.get(appConfig.getFilesPath() + fileName);
    }

}
