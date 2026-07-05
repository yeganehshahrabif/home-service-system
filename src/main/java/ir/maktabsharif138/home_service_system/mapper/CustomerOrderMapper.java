package ir.maktabsharif138.home_service_system.mapper;

import ir.maktabsharif138.home_service_system.dto.request.OrderCreateRequest;
import ir.maktabsharif138.home_service_system.dto.response.CustomerOrderResponse;
import ir.maktabsharif138.home_service_system.dto.response.ExpertOrderHistoryDetailsResponse;
import ir.maktabsharif138.home_service_system.dto.response.OrderHistoryDetailsResponse;
import ir.maktabsharif138.home_service_system.dto.response.OrderHistorySummaryResponse;
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

    void updateCustomerOrder(@MappingTarget CustomerOrder customerOrder, OrderCreateRequest request);

    List<CustomerOrderResponse> toOrderResponse(List<CustomerOrder> customerOrders);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "homeServiceName", source = "homeService.name")
    OrderHistorySummaryResponse toOrderHistorySummaryResponse(CustomerOrder order);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(
            target = "customerName",
            expression =
                    "java(order.getCustomer().getFirstName() + \" \" + order.getCustomer().getLastName())"
    )
    @Mapping(
            target = "expertId",
            expression =
                    "java(order.getAcceptedOffer() != null ? order.getAcceptedOffer().getExpert().getId() : null)"
    )
    @Mapping(
            target = "expertName",
            expression =
                    "java(order.getAcceptedOffer() != null ? " +
                            "order.getAcceptedOffer().getExpert().getFirstName() + \" \" +" +
                            "order.getAcceptedOffer().getExpert().getLastName() : null)"
    )
    @Mapping(target = "homeServiceId", source = "homeService.id")
    @Mapping(target = "homeServiceName", source = "homeService.name")
    @Mapping(target = "acceptedOffer", source = "acceptedOffer")
    @Mapping(target = "offers", source = "offers")
    OrderHistoryDetailsResponse toOrderHistoryDetailsResponse(CustomerOrder order);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(
            target = "customerName",
            expression =
                    "java(order.getCustomer().getFirstName() + \" \" + order.getCustomer().getLastName())"
    )
    @Mapping(
            target = "expertId",
            expression =
                    "java(order.getAcceptedOffer() != null ? order.getAcceptedOffer().getExpert().getId() : null)"
    )
    @Mapping(
            target = "expertName",
            expression =
                    "java(order.getAcceptedOffer() != null ? " +
                            "order.getAcceptedOffer().getExpert().getFirstName() + \" \" +" +
                            "order.getAcceptedOffer().getExpert().getLastName() : null)"
    )
    @Mapping(target = "homeServiceId", source = "homeService.id")
    @Mapping(target = "homeServiceName", source = "homeService.name")
    ExpertOrderHistoryDetailsResponse toExpertOrderHistoryDetailsResponse(CustomerOrder order);

}
