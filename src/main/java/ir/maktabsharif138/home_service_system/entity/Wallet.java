package ir.maktabsharif138.home_service_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet extends BaseEntity<Long> {

    private static final String CUSTOMER_COLUMN = "customer_id";
    private static final String EXPERT_COLUMN = "expert_id";

    private Double balance = 0.0;

    @OneToOne(mappedBy = "wallet")
    private Customer customer;

    @OneToOne(mappedBy = "wallet")
    private Expert expert;
}