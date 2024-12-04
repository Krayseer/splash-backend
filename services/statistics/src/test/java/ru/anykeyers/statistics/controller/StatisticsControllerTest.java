package ru.anykeyers.statistics.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.anykeyers.statistics.domain.key.CarWashMetricId;
import ru.anykeyers.statistics.service.StatisticsService;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StatisticsService statisticsService;

    private final Long carWashId = 1L;

    private final LocalDate date = LocalDate.of(2024, 12, 1);

    @BeforeEach
    void setUp() {
        CarWashMetricId metricId = new CarWashMetricId(carWashId, date);
//        CarWashMetric metric = new CarWashMetric(metricId, 5L, Map.of(1L, 100L));
//        when(statisticsService.getStatistics(carWashId)).thenReturn(List.of(metric));
    }

    @Test
    void testGetStatistics() throws Exception {
        mockMvc.perform(get("/statistics/{carWashId}", carWashId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ordersCount").value(5))
                .andExpect(jsonPath("$[0].countByServiceId['1']").value(100));
    }

}