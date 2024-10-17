package ru.anykeyers.configurationservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.configuration.BoxDTO;
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

    private final ModelMapper modelMapper;

    private final BoxRepository boxRepository;

    private final ConfigurationService configurationService;

    @Override
    public List<Box> getCarWashBoxes(Long carWashId) {
        Configuration configuration = configurationService.getConfiguration(carWashId);
        return boxRepository.findByConfiguration(configuration);
    }

    @Override
    public void addBox(BoxDTO boxDTO) {
        Box box = modelMapper.map(boxDTO, Box.class);
        boxRepository.save(box);
        log.info("Add box: {}", box);
    }

    @Override
    public void updateBox(BoxDTO boxDTO) {
        Box box = modelMapper.map(boxDTO, Box.class);
        boxRepository.save(box);
        log.info("Update box: {}", box);
    }

    @Override
    public void deleteBox(Long boxId) {
        boxRepository.deleteById(boxId);
        log.info("Delete box: {}", boxId);
    }

}
