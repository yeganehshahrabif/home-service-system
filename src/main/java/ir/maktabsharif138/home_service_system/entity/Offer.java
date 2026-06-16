package ir.maktabsharif138.home_service_system.entity;

import ir.maktabsharif138.home_service_system.entity.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Offer extends BaseEntity<Long> {

    private static final String CUSTOMER_ORDER_COLUMN = "customer_order_id";
    private static final String EXPERT_COLUMN = "expert_id";

    private BigDecimal proposedPrice;

    private LocalDateTime proposedStartTime;

    private Integer durationHours;

    @Enumerated(EnumType.STRING)
    private OfferStatus offerStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime offerDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CUSTOMER_ORDER_COLUMN)
    private CustomerOrder customerOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = EXPERT_COLUMN)
    private Expert expert;
}
