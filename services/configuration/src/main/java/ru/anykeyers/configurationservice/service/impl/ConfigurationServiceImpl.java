package ru.anykeyers.configurationservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.remote.RemoteStorageService;
import ru.anykeyers.configurationservice.UploadPhotoTask;
import ru.anykeyers.configurationservice.service.TaskManager;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.repository.ConfigurationRepository;
import ru.anykeyers.configurationservice.exception.ConfigurationNotFoundException;
import ru.anykeyers.configurationservice.exception.UserNotFoundConfigurationException;
import ru.anykeyers.configurationservice.service.ConfigurationService;

import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса обработки конфигураций
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

    private final TaskManager taskManager;

    private final ModelMapper modelMapper;

    private final RemoteStorageService remoteStorageService;

    private final ConfigurationRepository configurationRepository;

    @Override
    public List<Configuration> getAllConfigurations() {
        return configurationRepository.findAll();
    }

    @Override
    public Configuration getConfiguration(User user) {
        return getConfiguration(user.getId());
    }

    @Override
    public Configuration getConfiguration(UUID userId) {
        return configurationRepository.findByUserId(userId).orElseThrow(
                () -> new UserNotFoundConfigurationException(userId)
        );
    }

    @Override
    public Configuration getConfiguration(Long id) {
        return configurationRepository.findById(id).orElseThrow(() -> new ConfigurationNotFoundException(id));
    }

    @Override
    public void registerConfiguration(User user, ConfigurationDTO configurationDTO) {
        Configuration configuration = modelMapper.map(configurationDTO, Configuration.class);
        configurationRepository.save(configuration);
    }

    @Override
    public void updateConfiguration(ConfigurationDTO configurationDTO) {
        Configuration updatedConfiguration = modelMapper.map(configurationDTO, Configuration.class);
        configurationRepository.save(updatedConfiguration);
        if (configurationDTO.getPhotoUrls() != null) {
            taskManager.addTask(new UploadPhotoTask(remoteStorageService, configurationDTO.getPhotos(), configurationRepository, updatedConfiguration.getId()));
        }
        log.info("Update configuration: {}", updatedConfiguration);
    }

    @Override
    public void deleteConfiguration(User user) {
        Configuration configuration = getConfiguration(user);
        configurationRepository.delete(configuration);
        log.info("Deleted configuration: {}", configuration);
    }

}
