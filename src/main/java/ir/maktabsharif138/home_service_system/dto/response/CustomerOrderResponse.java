package ir.maktabsharif138.home_service_system.dto.response;

import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CustomerOrderResponse {
    private Long id;
    private String description;
    private BigDecimal proposedPrice;
    private LocalDateTime startDateTime;
    private String address;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Long customerId;
    private String customerName;
    private Long homeServiceId;
    private String homeServiceName;
    private OfferResponse acceptedOffer;
    private List<OfferResponse> offers;
}