package ru.anykeyers.configurationservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.anykeyers.commonsapi.domain.Address;
import ru.anykeyers.commonsapi.domain.configuration.OrganizationInfo;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.repository.ConfigurationRepository;
import ru.anykeyers.configurationservice.exception.ConfigurationNotFoundException;
import ru.anykeyers.configurationservice.exception.UserNotFoundConfigurationException;
import ru.anykeyers.configurationservice.service.ConfigurationService;
import ru.anykeyers.configurationservice.service.ReportWriter;
import ru.anykeyers.commonsapi.task.TaskService;
import ru.anykeyers.configurationservice.task.TaskFactory;
import ru.anykeyers.configurationservice.web.dto.ConfigurationRegisterRequest;
import ru.anykeyers.configurationservice.web.dto.ConfigurationUpdateRequest;
import ru.krayseer.storageclient.service.StorageClient;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Реализация сервиса обработки конфигураций
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    private final ConfigurationRepository configurationRepository;

    private final StorageClient storageClient;

    private final ReportWriter reportWriter;

    private final ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();

    private final TaskService taskService;

    private final TaskFactory taskFactory;

    @Override
    public List<Configuration> getAllConfigurations() {
        return configurationRepository.findAll();
    }

    @Override
    public Configuration getConfiguration(User user) {
        return getConfiguration(user.getId());
    }

    @Override
    @SneakyThrows
    public ByteArrayOutputStream getConfigurationPdf(UUID userId) {
        Configuration configuration = getConfiguration(userId);
        return reportWriter.generateReport(configuration);
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
    public void registerConfiguration(User user, ConfigurationRegisterRequest registerRequest) {
        OrganizationInfo organizationInfo = modelMapper.map(registerRequest, OrganizationInfo.class);
        Configuration configuration = Configuration.builder()
                .userId(user.getId())
                .organizationInfo(organizationInfo)
                .build();
        configurationRepository.save(configuration);
        taskService.addTask(taskFactory.createValidationTask(configuration, this::saveOrUpdateConfiguration));

    }

    @Override
    public void saveOrUpdateConfiguration(Configuration configuration) {
        configurationRepository.save(configuration);
    }

    @Override
    @SneakyThrows
    @Transactional
    public void updateConfiguration(User user, ConfigurationUpdateRequest updateRequest) {
        Configuration configuration = getConfiguration(user);
        configuration.setOpenTime(updateRequest.getOpenTime());
        configuration.setCloseTime(updateRequest.getCloseTime());
        OrganizationInfo organizationInfo = objectMapper.readValue(updateRequest.getOrganizationInfo(), OrganizationInfo.class);
        configuration.setOrganizationInfo(organizationInfo);
        configuration.setOrderProcessMode(updateRequest.getOrderProcessMode());
        if (!ObjectUtils.isEmpty(updateRequest.getAddress())) {
            Address address = objectMapper.readValue(updateRequest.getAddress(), Address.class);
            configuration.setAddress(address);
        }
        uploadConfigurationPhotos(configuration, updateRequest.getPhotos());
        uploadConfigurationVideo(configuration, updateRequest.getVideo());
        configurationRepository.save(configuration);
        log.info("Update configuration: {}", configuration);
    }

    @Override
    public void deleteConfiguration(User user) {
        Configuration configuration = getConfiguration(user);
        configurationRepository.delete(configuration);
        log.info("Deleted configuration: {}", configuration);
    }

    private void uploadConfigurationVideo(Configuration configuration, MultipartFile video) {
        if (video == null) {
            return;
        }
        threadPool.execute(() -> storageClient.uploadVideo(video, fileId -> {
            configuration.addVideo(fileId);
            configurationRepository.save(configuration);
            log.info("Success configuration video: {}", configuration.getVideoId());
        }, configuration.getUserId()));
    }

    private void uploadConfigurationPhotos(Configuration configuration, List<MultipartFile> photos) {
        if (CollectionUtils.isEmpty(photos)) {
            return;
        }
        threadPool.execute(() -> {
            for(MultipartFile photo: photos) {
                Consumer<String> callback = fileIds -> {
                    configuration.addPhotoUrls(List.of(fileIds));
                    configurationRepository.save(configuration);
                    log.info("Success upload configuration photos: {}", configuration.getPhotoUrls());
                };
                storageClient.uploadPhoto(photo, callback, configuration.getUserId());
            }
        });
    }

}
