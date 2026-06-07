package ir.maktabsharif138.home_service_system.mapper;

import ir.maktabsharif138.home_service_system.dto.request.OrderCreateRequest;
import ir.maktabsharif138.home_service_system.dto.response.CustomerOrderResponse;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerOrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "homeService", ignore = true)
    @Mapping(target = "offers", ignore = true)
    @Mapping(target = "acceptedOffer", ignore = true)
    CustomerOrder toCustomerOrder(OrderCreateRequest request);

    CustomerOrderResponse toCustomerOrderResponse(CustomerOrder customerOrder);

    void updateCustomerOrder(@MappingTarget CustomerOrder customerOrder, OrderCreateRequest request);

    List<CustomerOrderResponse> toOrderResponse(List<CustomerOrder> customerOrders);

}
