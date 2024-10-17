package ru.anykeyers.serviceofservices.processor;

import ru.anykeyers.serviceofservices.domain.service.ServiceEntity;
import ru.anykeyers.serviceofservices.domain.service.ServiceCreateRequest;
import ru.anykeyers.serviceofservices.domain.service.ServiceUpdateRequest;

import java.util.List;

/**
 * Сервис обработки услуг
 */
public interface ServiceProcessor {

    /**
     * Получить услугу
     *
     * @param serviceId идентификатор услуги
     */
    ServiceEntity getService(Long serviceId);

    /**
     * Получить список всех услуг автомойки
     *
     * @param carWashId идентификатор автомойки
     */
    List<ServiceEntity> getServices(Long carWashId);

    /**
     * Получить список услуг
     *
     * @param serviceIds идентификаторы услуг
     */
    List<ServiceEntity> getServices(List<Long> serviceIds);

    /**
     * Получить длительность выполнения услуг
     *
     * @param serviceIds идентификаторы услуг
     */
    long getServicesDuration(List<Long> serviceIds);

    /**
     * Сохранить услугу
     *
     * @param serviceCreateRequest даннные об услуге
     */
    void saveService(ServiceCreateRequest serviceCreateRequest);

    /**
     * Обновить услугу
     *
     * @param serviceUpdateRequest обновленные данные об услуге
     */
    void updateService(ServiceUpdateRequest serviceUpdateRequest);

    /**
     * Удалить услугу
     *
     * @param serviceId идентификатор услуги
     */
    void deleteService(Long serviceId);

}
