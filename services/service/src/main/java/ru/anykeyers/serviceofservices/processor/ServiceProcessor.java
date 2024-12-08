package ru.anykeyers.serviceofservices.processor;

import ru.anykeyers.commonsapi.domain.service.ServiceDTO;
import ru.anykeyers.serviceofservices.domain.ServiceEntity;

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
     * @param carWashId     идентификатор автомойки
     * @param serviceDTO    даннные об услуге
     */
    void saveService(Long carWashId, ServiceDTO serviceDTO);

    /**
     * Обновить услугу
     *
     * @param serviceDTO обновленные данные об услуге
     */
    void updateService(ServiceDTO serviceDTO);

    /**
     * Удалить услугу
     *
     * @param serviceId идентификатор услуги
     */
    void deleteService(Long serviceId);

}
