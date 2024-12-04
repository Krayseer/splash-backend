package ru.anykeyers.statistics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anykeyers.statistics.domain.Statistics;
import ru.anykeyers.statistics.service.StatisticsService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.BASE_CONTROLLER)
@Tag(name = "Обработка статистики")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "Получить метрики по автомойке")
    @GetMapping("/{carWashId}")
    public Statistics getStatistics(
            @Parameter(description = "Идентификатор автомойки") @PathVariable Long carWashId
    ) {
        return statisticsService.getStatistics(carWashId);
    }

}
