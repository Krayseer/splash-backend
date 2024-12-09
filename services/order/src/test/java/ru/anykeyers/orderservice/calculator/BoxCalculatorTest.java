package ru.anykeyers.orderservice.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.anykeyers.commonsapi.domain.Interval;
import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.domain.exception.BoxFreeNotFoundException;

import java.util.List;

/**
 * Тесты для {@link BoxCalculator}
 */
@ExtendWith(MockitoExtension.class)
class BoxCalculatorTest {

    @Mock
    private RemoteConfigurationService remoteConfigurationService;

    @InjectMocks
    private BoxCalculator boxCalculator;

    /**
     * Проверка получения идентификатора бокса для заказа с одним свободным боксом на установленное время
     */
    @Test
    void getBoxForOrder() {
        // Подготовка
        Long carWashId = 1L;
        Order firstBoxOrder = Order.builder()
                .startTime(200)
                .endTime(750)
                .boxId(2L)
                .build();
        Order secondBoxOrder = Order.builder()
                .startTime(900)
                .endTime(1000)
                .boxId(3L)
                .build();

        // Действие
        long actualBoxId = boxCalculator.findFreeBox(
                carWashId, List.of(firstBoxOrder, secondBoxOrder), Interval.of(700L, 800L)
        );

        // Проверка
        Assertions.assertEquals(3L, actualBoxId);
    }

    /**
     * Проверка выбрасывания ошибки не существования бокса, когда время заказа полностью входит в интервал существующего
     * заказа
     */
    @Test
    void getBoxForOrder_enterInAnotherOrder() {
        // Подготовка
        Long carWashId = 1L;
        Order firstBoxOrder = Order.builder()
                .startTime(200)
                .endTime(1200)
                .boxId(2L)
                .build();

        // Действие
        BoxFreeNotFoundException exception = Assertions.assertThrows(
                BoxFreeNotFoundException.class,
                () -> boxCalculator.findFreeBox(carWashId, List.of(firstBoxOrder), Interval.of(700L, 800L))
        );

        // Проверка
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("Free box not found", exception.getReason());
    }

    /**
     * Проверка выбрасывания ошибки не существования бокса, когда время заказа пересекается с существующим заказов
     */
    @Test
    void getBoxForOrder_intersectsWithAnotherOrder() {
        // Подготовка
        Long carWashId = 1L;
        Order firstBoxOrder = Order.builder()
                .startTime(700L)
                .endTime(900L)
                .boxId(2L)
                .build();

        // Действие
        BoxFreeNotFoundException exception = Assertions.assertThrows(
                BoxFreeNotFoundException.class,
                () -> boxCalculator.findFreeBox(carWashId, List.of(firstBoxOrder), Interval.of(600L, 800L))
        );

        // Проверка
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("Free box not found", exception.getReason());
    }

}