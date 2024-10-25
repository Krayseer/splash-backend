package ru.anykeyers.commonsapi.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Удаленный сервис обработки услуг
 */
@FeignClient(
        name = "service-of-services",
        path = "/api/service"
)
public interface RemoteServicesService {

    @GetMapping("/car-wash/{carWashId}")
    List<ServiceDTO> getServices(@PathVariable Long carWashId);

    @GetMapping("/list")
    List<ServiceDTO> getServices(@RequestParam("service-ids") List<Long> serviceIds);

    @GetMapping("/duration")
    long getServicesDuration(@RequestParam("service-ids") List<Long> serviceIds);

}
