package ir.maktabsharif138.home_service_system.entity;

import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrder extends BaseEntity<Long>{

    private static final String CUSTOMER_COLUMN = "customer_id";
    private static final String HOME_SERVICE_COLUMN = "home_service_id";
    private static final String ACCEPTED_OFFER_COLUMN = "accepted_offer_id";

    private String description;

    private BigDecimal proposedPrice;

    private LocalDateTime startDateTime;

    private String address;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CUSTOMER_COLUMN)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = HOME_SERVICE_COLUMN)
    private HomeService homeService;

    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Offer> offers = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ACCEPTED_OFFER_COLUMN)
    private Offer acceptedOffer;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

    @Column(precision = 19, scale = 4)
    private BigDecimal finalPrice;

}
