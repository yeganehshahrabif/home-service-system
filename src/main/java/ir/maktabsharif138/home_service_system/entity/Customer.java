package ir.maktabsharif138.home_service_system.entity;

import ir.maktabsharif138.home_service_system.entity.enums.AccountStatus;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseUser{

    private static final String WALLET_COLUMN = "wallet_id";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = WALLET_COLUMN, unique = true)
    private Wallet wallet;





}
