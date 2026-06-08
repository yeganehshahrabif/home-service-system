package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import java.util.List;

public interface CustomerOrderCoreService {

    CustomerOrder createOrder(CustomerOrder order);
    CustomerOrder findById(Long orderId);
    List<CustomerOrder> findByCustomerId(Long customerId);
    CustomerOrder update(CustomerOrder order);
    CustomerOrder startOrder(Long orderId);   // داخلش چک وضعیت و زمان
    CustomerOrder completeOrder(Long orderId); // داخلش چک وضعیت
    List<CustomerOrder> findByStatus(OrderStatus status);
    List<CustomerOrder> findAvailableOrdersForExpert(Long expertId);
}