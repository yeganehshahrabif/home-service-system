package ir.maktabsharif138.home_service_system.mapper;

import ir.maktabsharif138.home_service_system.dto.request.OrderCreateRequest;
import ir.maktabsharif138.home_service_system.dto.response.CustomerOrderResponse;
import ir.maktabsharif138.home_service_system.dto.response.ExpertOrderHistoryResponse;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = OfferMapper.class)
public interface CustomerOrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "homeService", ignore = true)
    @Mapping(target = "offers", ignore = true)
    @Mapping(target = "acceptedOffer", ignore = true)
    CustomerOrder toCustomerOrder(OrderCreateRequest request);

    @Mapping(
            target = "customerId",
            source = "customer.id"
    )
    @Mapping(
            target = "homeServiceId",
            source = "homeService.id"
    )
    @Mapping(
            target = "customerName",
            expression =
                    "java(customerOrder.getCustomer().getFirstName() + \" \" + customerOrder.getCustomer().getLastName())"
    )
    @Mapping(
            target = "homeServiceName",
            source = "homeService.name"
    )
    @Mapping(target = "offers", source = "offers")
    @Mapping(target = "acceptedOffer", source = "acceptedOffer")
    CustomerOrderResponse toCustomerOrderResponse(CustomerOrder customerOrder);


    @Mapping(
            target = "customerName",
            expression =
                    "java(customerOrder.getCustomer().getFirstName() + \" \" + customerOrder.getCustomer().getLastName())"
    )
    @Mapping(
            target = "homeServiceName",
            source = "homeService.name"
    )
    @Mapping(
            target = "acceptedOfferId",
            source = "acceptedOffer.id"
    )
    ExpertOrderHistoryResponse toExpertOrderHistoryResponse(CustomerOrder customerOrder);

    void updateCustomerOrder(@MappingTarget CustomerOrder customerOrder, OrderCreateRequest request);

    List<CustomerOrderResponse> toOrderResponse(List<CustomerOrder> customerOrders);

}
