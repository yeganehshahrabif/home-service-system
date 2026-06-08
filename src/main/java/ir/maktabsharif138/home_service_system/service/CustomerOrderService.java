
package ir.maktabsharif138.home_service_system.service;
import ir.maktabsharif138.home_service_system.dto.request.OrderCreateRequest;
import ir.maktabsharif138.home_service_system.dto.response.CustomerOrderResponse;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import java.util.List;

public interface CustomerOrderService {

    CustomerOrderResponse createOrder(Long customerId, OrderCreateRequest request);
    List<CustomerOrderResponse> getOrdersByCustomer(Long customerId);
    CustomerOrderResponse startOrder(Long orderId);
    CustomerOrderResponse completeOrder(Long orderId);

    List<CustomerOrderResponse> getOrdersByStatus(OrderStatus status);


    List<CustomerOrderResponse> getAvailableOrdersForExpert(Long expertId);
}