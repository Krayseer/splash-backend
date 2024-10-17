package ru.anykeyers.serviceofservices.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.anykeyers.serviceofservices.domain.service.ServiceUpdateRequest;
import ru.anykeyers.serviceofservices.repository.ServiceRepository;
import ru.anykeyers.serviceofservices.domain.service.ServiceEntity;
import ru.anykeyers.serviceofservices.domain.service.ServiceCreateRequest;
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

    @Override
    public ServiceEntity getService(Long serviceId) {
        return serviceRepository.findById(serviceId).orElseThrow(() -> new ServiceNotFoundException(serviceId));
    }

    @Override
    public List<ServiceEntity> getServices(Long carWashId) {
        return serviceRepository.findByCarWashId(carWashId);
    }

    @Override
    public List<ServiceEntity> getServices(List<Long> serviceIds) {
        return serviceRepository.findByIdIn(serviceIds);
    }

    @Override
    public long getServicesDuration(List<Long> serviceIds) {
        return getServices(serviceIds).stream().mapToLong(ServiceEntity::getDuration).sum();
    }

    @Override
    public void saveService(ServiceCreateRequest serviceCreateRequest) {
        ServiceEntity service = ServiceEntity.builder()
                .carWashId(serviceCreateRequest.getCarWashId())
                .name(serviceCreateRequest.getName())
                .duration(serviceCreateRequest.getDuration())
                .price(serviceCreateRequest.getPrice())
                .build();
        serviceRepository.save(service);
        log.info("Saving service: {}", service);
    }

    @Override
    public void updateService(ServiceUpdateRequest serviceUpdateRequest) {
        ServiceEntity service = getService(serviceUpdateRequest.getId());
        service.setDuration(serviceUpdateRequest.getDuration());
        service.setName(serviceUpdateRequest.getName());
        service.setPrice(serviceUpdateRequest.getPrice());
        serviceRepository.save(service);
        log.info("Updating service: {}", service);
    }

    @Override
    public void deleteService(Long serviceId) {
        serviceRepository.deleteById(serviceId);
        log.info("Deleting service: {}", serviceId);
    }

}
