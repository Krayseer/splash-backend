package ru.anykeyers.configurationservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.configuration.BoxDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.exception.BoxNotFoundException;
import ru.anykeyers.configurationservice.service.ConfigurationService;
import ru.anykeyers.configurationservice.domain.Box;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.repository.BoxRepository;
import ru.anykeyers.configurationservice.service.BoxService;

import java.util.List;

/**
 * Реализация сервиса обработки боксов
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BoxServiceImpl implements BoxService {

    private final BoxRepository boxRepository;

    private final ConfigurationService configurationService;

    @Override
    public Box getBox(Long boxId) {
        return boxRepository.findById(boxId).orElseThrow(() -> new BoxNotFoundException(boxId));
    }

    @Override
    public List<Box> getCarWashBoxes(Long carWashId) {
        Configuration configuration = configurationService.getConfiguration(carWashId);
        return boxRepository.findByConfiguration(configuration);
    }

    @Override
    public void addBox(User user, BoxDTO boxDTO) {
        Box box = Box.builder()
                .name(boxDTO.getName())
                .configuration(configurationService.getConfiguration(user))
                .build();
        boxRepository.save(box);
        log.info("Add box: {}", box);
    }

    @Override
    public void updateBox(BoxDTO boxDTO) {
        Box box = getBox(boxDTO.getId());
        box.setName(boxDTO.getName());
        boxRepository.save(box);
        log.info("Update box: {}", box);
    }

    @Override
    public void deleteBox(Long boxId) {
        boxRepository.deleteById(boxId);
        log.info("Delete box: {}", boxId);
    }

}