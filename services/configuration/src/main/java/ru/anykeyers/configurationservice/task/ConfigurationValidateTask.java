package ru.anykeyers.configurationservice.task;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import ru.anykeyers.commonsapi.MessageQueue;
import ru.anykeyers.commonsapi.task.Task;
import ru.anykeyers.commonsapi.task.TaskState;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.service.ValidationService;
import ru.anykeyers.configurationservice.web.mapper.ConfigurationMapper;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Задание валидации конфигурации
 */
@Slf4j
public class ConfigurationValidateTask implements Task {

    /**
     * Название задания
     */
    private static final String TASK_NAME = "ConfigurationValidateTask";

    /**
     * Конфигурация автомойки для валидации
     */
    private final Configuration configuration;

    /**
     * Сервис валидации
     */
    private final ValidationService validationService;

    /**
     * Маппер для конфигураций автомоек
     */
    private final ConfigurationMapper configurationMapper;

    /**
     * Отправитель сообщений Kafka
     */
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Слушатель обработки результата
     */
    private Consumer<Configuration> resultListener;

    /**
     * Состояние выполнения задания
     */
    private volatile TaskState taskState = TaskState.PENDING;

    public ConfigurationValidateTask(ValidationService validationService,
                                     KafkaTemplate<String, Object> kafkaTemplate,
                                     ConfigurationMapper configurationMapper,
                                     Configuration configuration) {
        this.validationService = validationService;
        this.kafkaTemplate = kafkaTemplate;
        this.configurationMapper = configurationMapper;
        this.configuration = configuration;
    }

    @Override
    public String getName() {
        return TASK_NAME;
    }

    @Override
    public TaskState getTaskState() {
        return taskState;
    }

    @Override
    public UUID getUserId() {
        return configuration.getUserId();
    }

    @Override
    public int getPercentage() {
        return taskState.equals(TaskState.COMPLETE) ? 100 : 0;
    }

    @Override
    @SneakyThrows
    public void run() {
        if (taskState == TaskState.PENDING) {
            taskState = TaskState.IN_PROCESS;
        }
        boolean isTinValid = validationService.validateTinConfiguration(configuration);
        configuration.setValid(isTinValid);
        kafkaTemplate.send(MessageQueue.CAR_WASH_TIN_VALIDATE_RESULT, configurationMapper.toDTO(configuration));
        log.info("Send notification about validate configuration {} with result {}" , configuration, isTinValid);
        if (resultListener != null) {
            resultListener.accept(configuration);
        }
        taskState = TaskState.COMPLETE;
    }

    /**
     * Добавить слушатель обработки результата выполнения задания
     *
     * @param listener слушатель
     */
    public void addResultListener(Consumer<Configuration> listener) {
        this.resultListener = listener;
    }

}
