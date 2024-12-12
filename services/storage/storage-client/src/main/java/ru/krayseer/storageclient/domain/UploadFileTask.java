package ru.krayseer.storageclient.domain;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import ru.anykeyers.commonsapi.task.Task;
import ru.anykeyers.commonsapi.task.TaskState;
import ru.anykeyers.storageproto.FileType;
import ru.krayseer.storageproto.proto.FileChunk;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Задание загрузки файла
 */
@Slf4j
public class UploadFileTask implements Task {

    /**
     * Название задания
     */
    private static final String TASK_NAME = "UploadFileTask";

    /**
     * Данные файла
     */
    private final InputStream fileStream;

    /**
     * Тип файла
     */
    private final FileType fileType;

    /**
     * Создатель обсервера загрузки чанков файла
     */
    private final Supplier<StreamObserver<FileChunk>> observerCreator;

    /**
     * Исходный размер файла
     */
    private final long totalSize;

    /**
     * Процент выполнения задачи
     */
    private volatile int percentage;

    /**
     * Состояние обработки задачи
     */
    private volatile TaskState state = TaskState.PENDING;

    /**
     * Идентификатор пользователя
     */
    private final UUID userId;

    @SneakyThrows
    public UploadFileTask(MultipartFile file, FileType fileType, UUID userId,
                          Supplier<StreamObserver<FileChunk>> observerCreator) {
        this.fileStream = new ByteArrayInputStream(file.getInputStream().readAllBytes());
        this.fileType = fileType;
        this.observerCreator = observerCreator;
        this.userId = userId;
        this.totalSize = file.getSize();
    }

    @Override
    public String getName() {
        return TASK_NAME;
    }

    @Override
    public TaskState getTaskState() {
        return state;
    }

    @Override
    public UUID getUserId() {
        return userId;
    }

    @Override
    public int getPercentage() {
        return percentage;
    }

    @Override
    @SneakyThrows
    public void run() {
        try {
            startTask();
            processFileChunks();
            completeTask();
        } catch (Exception e) {
            handleError(e);
        } finally {
            closeResources();
        }
    }

    /**
     * Инициализация задачи, изменение состояния на IN_PROCESS.
     */
    private void startTask() {
        if (state == TaskState.PENDING) {
            state = TaskState.IN_PROCESS;
        }
    }

    /**
     * Чтение и обработка файла, отправка чанков через StreamObserver.
     */
    @SneakyThrows
    private void processFileChunks() {
        byte[] buffer = new byte[4096];
        int bytesRead;
        long bytesUploaded = 0;
        StreamObserver<FileChunk> observer = observerCreator.get();

        while ((bytesRead = fileStream.read(buffer)) != -1) {
            bytesUploaded += bytesRead;
            updateProgress(bytesUploaded);
            sendChunk(observer, buffer, bytesRead);
        }
        observer.onCompleted();
    }

    /**
     * Обновление прогресса выполнения задачи.
     *
     * @param currentByteSize текущий объем обработанных байтов.
     */
    private void updateProgress(long currentByteSize) {
        percentage = (int) ((currentByteSize * 100) / totalSize);
    }

    /**
     * Создание и отправка чанка файла через StreamObserver.
     *
     * @param observer  StreamObserver для отправки данных.
     * @param buffer    буфер данных.
     * @param bytesRead количество прочитанных байтов.
     */
    private void sendChunk(StreamObserver<FileChunk> observer, byte[] buffer, int bytesRead) {
        FileChunk chunk = FileChunk.newBuilder()
                .setContent(ByteString.copyFrom(buffer, 0, bytesRead))
                .setFormat(fileType.name())
                .build();
        observer.onNext(chunk);
    }

    /**
     * Завершение задачи, установка состояния в COMPLETE
     */
    private void completeTask() {
        state = TaskState.COMPLETE;
    }

    /**
     * Обработка ошибок
     *
     * @param e исключение, возникшее в процессе выполнения.
     */
    private void handleError(Exception e) {
        log.error("Error occurred during task execution: {}", e.getMessage(), e);
        state = TaskState.FAILED;
    }

    /**
     * Закрытие ресурсов, таких как потоки
     */
    @SneakyThrows
    private void closeResources() {
        if (fileStream != null) {
            fileStream.close();
        }
    }


}
