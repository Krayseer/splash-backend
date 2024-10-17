//package ru.anykeyers.businessorderservice.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.anykeyers.businessorderservice.repository.BusinessOrderRepository;
//import ru.anykeyers.businessorderservice.domain.BusinessOrder;
//import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
//import ru.anykeyers.commonsapi.domain.order.OrderDTO;
//import ru.anykeyers.commonsapi.domain.user.UserDTO;
//import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
//import ru.anykeyers.commonsapi.remote.RemoteOrderService;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
///**
// * Тесты для {@link OrderService}
// */
//@ExtendWith(MockitoExtension.class)
//class OrderServiceTest {
//
//    @Mock
//    private RemoteOrderService remoteOrderService;
//
//    @Mock
//    private BusinessOrderRepository businessOrderRepository;
//
//    @Mock
//    private RemoteConfigurationService remoteConfigurationService;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    private final UserDTO employee = UserDTO.builder().id(5L).username("employee-name").build();
//
//    private final OrderDTO testOrder = OrderDTO.builder()
//            .id(5L)
//            .carWashId(19L)
//            .startTime("1970-01-02T09:00:00Z")
//            .endTime("1970-01-02T12:30:00Z")
//            .build();
//
//    /**
//     * Тест обработки создания заказа при включенной функции обработки заказов менеджерами у автомоек
//     */
//    @Test
//    void processOrderCreateWithManagementEnabledByCarWash() {
//        Long carWashId = 1L;
//        ConfigurationDTO configuration = ConfigurationDTO.builder().id(carWashId).managementProcessOrders(true).build();
//        OrderDTO order = OrderDTO.builder().carWashId(carWashId).build();
//
//        Mockito.when(remoteConfigurationService.getConfiguration(carWashId)).thenReturn(configuration);
//        orderService.processOrderCreate(order);
//
//        Mockito.verify(remoteConfigurationService, Mockito.never()).getEmployees(Mockito.any());
//        Mockito.verify(remoteOrderService, Mockito.never()).getOrders(Mockito.any(), Mockito.any());
//        Mockito.verify(businessOrderRepository, Mockito.never()).save(Mockito.any());
//        Mockito.verify(eventService, Mockito.never()).sendOrderApplyEmployeeEvent(Mockito.any());
//    }
//
//    /**
//     * Тест обработки создания заказа и назначения ему работника в связи с отсутствием заказов на указанную дату
//     */
//    @Test
//    void processOrderCreateWithEmptyOrders() {
//        Long carWashId = 1L;
//        String startTime = "1970-01-02T00:00:00Z";
//        ConfigurationDTO configuration = ConfigurationDTO.builder().id(carWashId).build();
//        OrderDTO order = OrderDTO.builder().id(2L).carWashId(carWashId).startTime(startTime).build();
//
//        Mockito.when(remoteConfigurationService.getConfiguration(carWashId)).thenReturn(configuration);
//        Mockito.when(remoteConfigurationService.getEmployees(carWashId)).thenReturn(List.of(employee));
//        Mockito.when(remoteOrderService.getOrders(carWashId, "02-01-1970")).thenReturn(Collections.emptyList());
//
//        orderService.processOrderCreate(order);
//
//        Mockito.verify(eventService, Mockito.never()).sendOrderRemoveEvent(Mockito.any());
//        Mockito.verify(businessOrderRepository, Mockito.times(1)).save(Mockito.any());
//        Mockito.verify(eventService, Mockito.times(1)).sendOrderApplyEmployeeEvent(order);
//    }
//
//    /**
//     * Тест обработки создания заказа со свободными работниками
//     */
//    @Test
//    void processOrderCreateWithExistsFreeEmployees() {
//        Long carWashId = 1L;
//        String startTime = "1970-01-02T13:00:00Z";
//        String endTime = "1970-01-02T15:30:00Z";
//        ConfigurationDTO configuration = ConfigurationDTO.builder().id(carWashId).build();
//        OrderDTO order = OrderDTO.builder()
//                .id(2L)
//                .carWashId(carWashId)
//                .startTime(startTime)
//                .endTime(endTime)
//                .build();
//
//        Mockito.when(remoteConfigurationService.getConfiguration(carWashId)).thenReturn(configuration);
//        Mockito.when(remoteConfigurationService.getEmployees(carWashId)).thenReturn(List.of(employee));
//        Mockito.when(remoteOrderService.getOrders(carWashId, "02-01-1970")).thenReturn(List.of(testOrder));
//
//        orderService.processOrderCreate(order);
//
//        Mockito.verify(eventService, Mockito.never()).sendOrderRemoveEvent(Mockito.any());
//        Mockito.verify(businessOrderRepository, Mockito.times(1)).save(Mockito.any());
//        Mockito.verify(eventService, Mockito.times(1)).sendOrderApplyEmployeeEvent(Mockito.any());
//    }
//
//    /**
//     * Тест обработки создания заказа без свободных работников
//     */
//    @Test
//    void processOrderCreateWithNotExistsFreeEmployees() {
//        Long carWashId = 1L;
//        String startTime = "1970-01-02T08:00:00Z";
//        String endTime = "1970-01-02T11:30:00Z";
//        ConfigurationDTO configuration = ConfigurationDTO.builder().id(carWashId).build();
//        OrderDTO order = OrderDTO.builder()
//                .id(2L)
//                .carWashId(carWashId)
//                .startTime(startTime)
//                .endTime(endTime)
//                .build();
//
//        Mockito.when(remoteConfigurationService.getConfiguration(carWashId)).thenReturn(configuration);
//        Mockito.when(remoteConfigurationService.getEmployees(carWashId)).thenReturn(List.of(employee));
//        Mockito.when(businessOrderRepository.findByOrderId(Mockito.any())).thenReturn(new BusinessOrder(testOrder.getId(), 4L));
//        Mockito.when(remoteOrderService.getOrders(carWashId, "02-01-1970")).thenReturn(List.of(testOrder));
//
//        orderService.processOrderCreate(order);
//
//        Mockito.verify(eventService, Mockito.never()).sendOrderRemoveEvent(Mockito.any());
//        Mockito.verify(businessOrderRepository, Mockito.times(1)).save(Mockito.any());
//        Mockito.verify(eventService, Mockito.times(1)).sendOrderApplyEmployeeEvent(order);
//    }
//
//    /**
//     * Тест назначения работника заказу
//     */
//    @Test
//    void appointOrderEmployee() {
//        OrderDTO order = OrderDTO.builder().id(2L).build();
//        orderService.appointOrderEmployee(order, 2L);
//        Mockito.verify(businessOrderRepository, Mockito.times(1)).save(Mockito.any());
//        Mockito.verify(eventService, Mockito.times(1)).sendOrderApplyEmployeeEvent(order);
//    }
//
//    /**
//     * Тест удаления работника с несуществующего бизнес заказа
//     */
//    @Test
//    void disappointEmployeeFromNotExistsBusinessOrder() {
//        Mockito.when(businessOrderRepository.findById(1L)).thenReturn(Optional.empty());
//
//        RuntimeException exception = Assertions.assertThrows(
//                RuntimeException.class, () -> orderService.disappointEmployeeFromOrder(1L)
//        );
//        Assertions.assertEquals("Order not found", exception.getMessage());
//    }
//
//    /**
//     * Тест успешного удаления работника с бизнес заказа
//     */
//    @Test
//    void disappointEmployeeFromOrder() {
//        BusinessOrder order = new BusinessOrder(1L, 2L, 3L);
//        Mockito.when(businessOrderRepository.findById(1L)).thenReturn(Optional.of(order));
//
//        orderService.disappointEmployeeFromOrder(1L);
//
//        Mockito.verify(eventService, Mockito.times(1)).sendOrderDisappointEmployeeEvent("2");
//        Mockito.verify(businessOrderRepository, Mockito.times(1)).deleteById(1L);
//    }
//
//}