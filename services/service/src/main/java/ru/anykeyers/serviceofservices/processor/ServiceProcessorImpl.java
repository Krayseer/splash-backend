package ru.anykeyers.serviceofservices.processor;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;
import ru.anykeyers.serviceofservices.repository.ServiceRepository;
import ru.anykeyers.serviceofservices.domain.ServiceEntity;
import ru.anykeyers.serviceofservices.domain.exception.ServiceNotFoundException;

import java.util.List;

/**
 * Реализация сервиса обработки услуг
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceProcessorImpl implements ServiceProcessor {

    private final ServiceRepository serviceRepository;

    private final VersionGenerator versionGenerator;

    @Override
    public ServiceEntity getService(Long serviceId) {
        return serviceRepository.findById(serviceId).orElseThrow(() -> new ServiceNotFoundException(serviceId));
    }

    @Override
    public List<ServiceEntity> getServices(Long carWashId) {
        return serviceRepository.findByCarWashIdAndActualTrue(carWashId);
    }

    @Override
    public List<ServiceEntity> getServices(List<Long> serviceIds) {
        return serviceRepository.findByIdIn(serviceIds);
    }

    @Override
    public List<ServiceEntity> getServiceHistory(Long serviceId) {
        ServiceEntity service = getService(serviceId);
        return service.getOriginalServiceId() == null
                ? serviceRepository.findServicesByIdOrOriginalId(serviceId)
                : serviceRepository.findServicesByIdOrOriginalId(service.getOriginalServiceId());
    }

    @Override
    public long getServicesDuration(List<Long> serviceIds) {
        return getServices(serviceIds).stream().mapToLong(ServiceEntity::getDuration).sum();
    }

    @Override
    public void saveService(Long carWashId, ServiceDTO serviceDTO) {
        ServiceEntity service = ServiceEntity.builder()
                .carWashId(carWashId)
                .name(serviceDTO.getName())
                .duration(serviceDTO.getDuration())
                .price(serviceDTO.getPrice())
                .actual(true)
                .build();
        serviceRepository.save(service);
        log.info("Add new service: {}", service);
    }

    @Override
    @Transactional
    public void updateService(ServiceDTO serviceDTO) {
        ServiceEntity currentService = getService(serviceDTO.getId());
        currentService.setActual(false);
        serviceRepository.save(currentService);

        ServiceEntity updatedService = ServiceEntity.builder()
                .originalServiceId(currentService.getOriginalServiceId() == null ? currentService.getId() : currentService.getOriginalServiceId())
                .carWashId(currentService.getCarWashId())
                .name(serviceDTO.getName())
                .duration(serviceDTO.getDuration())
                .price(serviceDTO.getPrice())
                .version(versionGenerator.generateVersion(currentService.getVersion()))
                .actual(true)
                .build();

        updatedService.setDuration(serviceDTO.getDuration());
        updatedService.setName(serviceDTO.getName());
        updatedService.setPrice(serviceDTO.getPrice());
        serviceRepository.save(updatedService);
        log.info("Update service: {}", updatedService);
    }

    @Override
    public void deleteService(Long serviceId) {
        serviceRepository.deleteById(serviceId);
        log.info("Deleting service: {}", serviceId);
    }

}
