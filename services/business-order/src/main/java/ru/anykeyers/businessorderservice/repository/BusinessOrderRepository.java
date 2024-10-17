package ru.anykeyers.businessorderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anykeyers.businessorderservice.domain.BusinessOrder;

import java.util.List;
import java.util.UUID;

/**
 * DAO для работы с бизнес заказами
 */
@Repository
public interface BusinessOrderRepository extends JpaRepository<BusinessOrder, Long> {

    /**
     * Получить бизнес заказ
     *
     * @param id идентификатор оригинального заказа
     */
    BusinessOrder findByOrderId(Long id);

    /**
     * Получить список бизнес заказов работника
     *
     * @param employeeId идентификатор работника
     */
    List<BusinessOrder> findByEmployeeId(UUID employeeId);

}
