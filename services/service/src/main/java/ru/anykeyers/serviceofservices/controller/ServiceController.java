package ru.anykeyers.serviceofservices.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.serviceofservices.domain.ServiceEntity;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;
import ru.anykeyers.serviceofservices.processor.ServiceProcessor;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.SERVICE_URL)
@Tag(name = "Обработка услуг")
public class ServiceController {

    private final ModelMapper modelMapper;

    private final ServiceProcessor serviceProcessor;

    @Operation(summary = "Получить данные об услуге")
    @GetMapping("/{id}")
    public ServiceDTO getService(
            @Parameter(description = "Идентификатор услуги") @PathVariable Long id
    ) {
        return modelMapper.map(serviceProcessor.getService(id), ServiceDTO.class);
    }

    @Operation(summary = "Получить историю изменений услуги")
    @GetMapping("/{id}/history")
    public List<ServiceDTO> getServiceHistory(
            @Parameter(description = "Идентификатор услуги") @PathVariable Long id
    ) {
        return convertToDTO(serviceProcessor.getServiceHistory(id));
    }

    @Operation(summary = "Получить список услуг")
    @GetMapping("/list")
    public List<ServiceDTO> getServices(
            @Parameter(name = "Идентификаторы услуг") @RequestParam("service-ids") Long[] serviceIds
    ) {
        return convertToDTO(serviceProcessor.getServices(Arrays.asList(serviceIds)));
    }

    @Operation(summary = "Получить продолжительность выполнения услуг")
    @GetMapping("/duration")
    public long getServicesDuration(
            @Parameter(name = "Идентификаторы услуг") @RequestParam("service-ids") Long[] serviceIds
    ) {
        return serviceProcessor.getServicesDuration(Arrays.asList(serviceIds));
    }

    @Operation(summary = "Получить все услуги автомойки")
    @GetMapping("/car-wash/{id}")
    public List<ServiceDTO> getCarWashServices(
            @Parameter(name = "Идентификатор автомойки") @PathVariable Long id
    ) {
        return convertToDTO(serviceProcessor.getServices(id));
    }

    @Operation(summary = "Сохранить услугу")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{carWashId}")
    public void saveService(
            @Parameter(name = "Идентификатор автомойки") @PathVariable Long carWashId,
            @Parameter(name = "Данные об услуге") @RequestBody @Valid ServiceDTO serviceDTO
    ) {
        serviceProcessor.saveService(carWashId, serviceDTO);
    }

    @Operation(summary = "Обновить услугу")
    @PutMapping
    public void updateService(
            @Parameter(name = "Обновленные данные об услуге") @Valid @RequestBody ServiceDTO serviceDTO
    ) {
       serviceProcessor.updateService(serviceDTO);
    }

    @Operation(summary = "Удалить услугу")
    @DeleteMapping("/{serviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(@Parameter(name = "Идентификатор услуги") @PathVariable Long serviceId) {
        serviceProcessor.deleteService(serviceId);
    }

    private List<ServiceDTO> convertToDTO(List<ServiceEntity> services) {
        return services.stream()
                .map(service -> modelMapper.map(service, ServiceDTO.class))
                .toList();
    }

}
