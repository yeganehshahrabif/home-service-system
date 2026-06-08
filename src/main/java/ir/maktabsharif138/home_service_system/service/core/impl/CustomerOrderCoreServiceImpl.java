package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.service.core.CustomerOrderCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerOrderCoreServiceImpl implements CustomerOrderCoreService {
    @Override
    public CustomerOrder createOrder(CustomerOrder order) {
        return null;
    }

    @Override
    public CustomerOrder findById(Long orderId) {
        return null;
    }

    @Override
    public List<CustomerOrder> findByCustomerId(Long customerId) {
        return List.of();
    }

    @Override
    public CustomerOrder update(CustomerOrder order) {
        return null;
    }

    @Override
    public CustomerOrder startOrder(Long orderId) {
        return null;
    }

    @Override
    public CustomerOrder completeOrder(Long orderId) {
        return null;
    }

    @Override
    public List<CustomerOrder> findByStatus(OrderStatus status) {
        return List.of();
    }

    @Override
    public List<CustomerOrder> findAvailableOrdersForExpert(Long expertId) {
        return List.of();
    }
}
