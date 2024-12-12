package ru.anykeyers.configurationservice.task;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.service.ValidationService;
import ru.anykeyers.configurationservice.web.mapper.ConfigurationMapper;

import java.util.function.Consumer;

/**
 * Фабрика создания заданий
 */
@Component
@RequiredArgsConstructor
public class TaskFactory {

    private final ValidationService validationService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ConfigurationMapper configurationMapper;

    /**
     * Создать задачу валидации конфигурации
     *
     * @param configuration конфигурация автомойки
     */
    public ConfigurationValidateTask createValidationTask(Configuration configuration,
                                                          Consumer<Configuration> resultListener) {
        ConfigurationValidateTask task = new ConfigurationValidateTask(
                validationService, kafkaTemplate, configurationMapper, configuration
        );
        task.addResultListener(resultListener);
        return task;
    }

}
