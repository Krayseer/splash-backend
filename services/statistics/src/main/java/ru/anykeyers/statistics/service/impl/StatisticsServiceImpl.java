package ru.anykeyers.statistics.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;
import ru.anykeyers.commonsapi.remote.RemoteServicesService;
import ru.anykeyers.statistics.domain.*;
import ru.anykeyers.statistics.domain.key.CarWashMetricId;
import ru.anykeyers.statistics.domain.key.KeyGenerator;
import ru.anykeyers.statistics.repository.MetricRepository;
import ru.anykeyers.statistics.service.ReportGenerator;
import ru.anykeyers.statistics.service.StatisticsService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Реализация сервиса статистики
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final MetricRepository metricRepository;

    private final KeyGenerator keyGenerator;

    private final RemoteServicesService remoteServicesService;

    private final ReportGenerator reportGenerator;

    @Override
    public Statistics getStatistics(Long carWashId) {
        List<CarWashMetric> metricsByDate = metricRepository.findByIdCarWashId(carWashId);
        Map<Long, ServiceStatistics> summaryStatisticsByServiceId = new HashMap<>();
        List<DateStatistics> dateStatistics = new ArrayList<>();
        metricsByDate.forEach(metric -> processMetric(metric, summaryStatisticsByServiceId, dateStatistics));
        SummaryStatistics summaryStatistics = buildSummaryStatistics(summaryStatisticsByServiceId);
        return new Statistics(summaryStatistics, dateStatistics);
    }

    @Override
    public byte[] getStatisticsPdf(Long carWashId) {
        Statistics statistics = getStatistics(carWashId);
        return reportGenerator.generate(statistics);
    }

    @Override
    @Transactional
    public void processOrder(OrderDTO orderDTO) {
        CarWashMetricId metricId = keyGenerator.generate(orderDTO);
        CarWashMetric metric = metricRepository.findById(metricId).orElseGet(
                () -> new CarWashMetric(metricId, 0L, new HashMap<>())
        );
        metric.setOrdersCount(metric.getOrdersCount() + 1);
        orderDTO.getServices().forEach(service -> {
            Long serviceId = service.getId();
            metric.getCountByServiceId().put(
                    serviceId,
                    metric.getCountByServiceId().getOrDefault(serviceId, 0L) + service.getPrice()
            );
        });
        metricRepository.save(metric);
        log.info("Process order: {}", orderDTO);
    }

    private void processMetric(CarWashMetric metric,
                               Map<Long, ServiceStatistics> summaryStatisticsByServiceId,
                               List<DateStatistics> dateStatistics) {
        Map<Long, ServiceDTO> serviceById = collectServicesById(new ArrayList<>(metric.getCountByServiceId().keySet()));
        updateSummaryStatistics(metric, serviceById, summaryStatisticsByServiceId);
        List<ServiceStatistics> serviceStatistics = buildServiceStatistics(metric, serviceById);
        dateStatistics.add(buildDateStatistics(metric, serviceStatistics));
    }

    private void updateSummaryStatistics(CarWashMetric metric,
                                         Map<Long, ServiceDTO> serviceById,
                                         Map<Long, ServiceStatistics> summaryStatisticsByServiceId) {
        metric.getCountByServiceId().forEach((serviceId, value) -> {
            ServiceDTO service = serviceById.get(serviceId);
            ServiceStatistics summaryStatistics = summaryStatisticsByServiceId.computeIfAbsent(service.getId(), id -> {
                ServiceStatistics stats = new ServiceStatistics();
                stats.setName(service.getName());
                return stats;
            });
            summaryStatistics.setSum(summaryStatistics.getSum() + value);
            summaryStatistics.setCount(summaryStatistics.getCount() + (value / service.getPrice()));
        });
    }

    private List<ServiceStatistics> buildServiceStatistics(CarWashMetric metric, Map<Long, ServiceDTO> serviceById) {
        return metric.getCountByServiceId().entrySet().stream()
                .map(entry -> {
                    ServiceDTO serviceDTO = serviceById.get(entry.getKey());
                    return new ServiceStatistics(
                            serviceDTO.getName(),
                            entry.getValue(),
                            entry.getValue() / serviceDTO.getPrice()
                    );
                })
                .toList();
    }

    private DateStatistics buildDateStatistics(CarWashMetric metric, List<ServiceStatistics> serviceStatistics) {
        return new DateStatistics(
                metric.getId().getDate().toString(),
                metric.getOrdersCount(),
                serviceStatistics.stream().mapToLong(ServiceStatistics::getCount).sum(),
                serviceStatistics.stream().mapToLong(ServiceStatistics::getSum).sum()
        );
    }

    private SummaryStatistics buildSummaryStatistics(Map<Long, ServiceStatistics> summaryStatisticsByServiceId) {
        List<ServiceStatistics> serviceStatistics = new ArrayList<>(summaryStatisticsByServiceId.values());
        SummaryStatistics summaryStatistics = new SummaryStatistics();
        summaryStatistics.setSummaryPrice(serviceStatistics.stream().mapToLong(ServiceStatistics::getSum).sum());
        summaryStatistics.setSummaryCount(serviceStatistics.stream().mapToLong(ServiceStatistics::getCount).sum());
        summaryStatistics.setServices(serviceStatistics);
        return summaryStatistics;
    }

    private Map<Long, ServiceDTO> collectServicesById(List<Long> serviceIds) {
        return remoteServicesService.getServices(serviceIds).stream()
                .collect(Collectors.toMap(ServiceDTO::getId, Function.identity()));
    }
}
