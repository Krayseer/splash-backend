package ru.anykeyers.serviceofservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.anykeyers.serviceofservices.domain.ServiceEntity;

import java.util.List;

/**
 * DAO для обработки услуг
 */
@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    /**
     * Получить список услуг автомойки
     *
     * @param id идентификатор автомойки
     */
    List<ServiceEntity> findByCarWashIdAndActualTrue(Long id);

    /**
     * Получить список услуг
     *
     * @param ids идентификаторы услуг
     */
    List<ServiceEntity> findByIdIn(List<Long> ids);

    @Query("SELECT s FROM ServiceEntity s WHERE s.id = :serviceId OR s.originalServiceId = :serviceId")
    List<ServiceEntity> findServicesByIdOrOriginalId(Long serviceId);

}
