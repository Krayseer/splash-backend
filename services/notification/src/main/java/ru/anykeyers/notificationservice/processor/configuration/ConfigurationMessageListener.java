package ru.anykeyers.notificationservice.processor.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.MessageQueue;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;

/**
 * Слушатель сообщений по автомойкам
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigurationMessageListener {

    private final ConfigurationProcessor processor;

    /**
     * Обработать сообщение проверки ИНН у автомойки
     *
     * @param configuration конфигурация автомойки
     */
    @KafkaListener(topics = MessageQueue.CAR_WASH_TIN_VALIDATE_RESULT)
    public void receiveConfigurationTinValidate(ConfigurationDTO configuration) {
        processor.processConfigurationValidate(configuration);
    }

}
