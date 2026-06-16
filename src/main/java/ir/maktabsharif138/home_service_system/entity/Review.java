package ir.maktabsharif138.home_service_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity<Long> {

    private static final String CUSTOMER_COLUMN = "customer_id";
    private static final String EXPERT_COLUMN = "expert_id";
    private static final String CUSTOMER_ORDER_COLUMN = "customer_order_id";

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CUSTOMER_COLUMN)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = EXPERT_COLUMN)
    private Expert expert;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CUSTOMER_ORDER_COLUMN, unique = true)
    private CustomerOrder customerOrder;


}
