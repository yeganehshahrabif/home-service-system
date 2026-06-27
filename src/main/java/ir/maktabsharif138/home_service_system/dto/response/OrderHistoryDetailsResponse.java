package ir.maktabsharif138.home_service_system.dto.response;

import ir.maktabsharif138.home_service_system.dto.response.OfferResponse;
import ir.maktabsharif138.home_service_system.entity.enums.OrderPaymentStatus;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderHistoryDetailsResponse {

    private Long orderId;
    private String description;
    private String address;

    private BigDecimal proposedPrice;
    private BigDecimal finalPrice;

    private LocalDateTime orderDate;
    private LocalDateTime startDateTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;

    private OrderStatus orderStatus;
    private OrderPaymentStatus orderPaymentStatus;

    private Long customerId;
    private String customerName;

    private Long expertId;
    private String expertName;

    private Long homeServiceId;
    private String homeServiceName;

    private OfferResponse acceptedOffer;
    private List<OfferResponse> offers;
}