package ru.anykeyers.businessorderservice.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.domain.configuration.OrderProcessMode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Фабрика по созданию обработчиков заказов
 */
@Component
public class OrderProcessorFactory {

    private final Map<OrderProcessMode, OrderProcessor> processorMap;

    @Autowired
    public OrderProcessorFactory(List<OrderProcessor> processors) {
        this.processorMap = processors.stream()
                .collect(Collectors.toMap(OrderProcessor::getOrderProcessMode, Function.identity()));
    }

    /**
     * Создать обработчик заказа
     *
     * @param mode режим обработки заказа
     */
    public OrderProcessor getProcessor(OrderProcessMode mode) {
        return processorMap.get(mode);
    }

}
