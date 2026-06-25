package ir.maktabsharif138.home_service_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Expert extends BaseUser{

    private static final String WALLET_COLUMN = "wallet_id";
    private static final String EXPERT_HOME_SERVICE = "expert_home_service";
    private static final String EXPERT_ID = "expert_id";
    private static final String HOME_SERVICE_ID = "home_service_id";

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = WALLET_COLUMN, unique = true)
    private Wallet wallet;

    @Column(length = 300 * 1024)
    private String profileImage;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = EXPERT_HOME_SERVICE,
        joinColumns = @JoinColumn(name = EXPERT_ID),
            inverseJoinColumns = @JoinColumn(name = HOME_SERVICE_ID)
    )
    private Set<HomeService> homeServices = new HashSet<>();

    private Double rating;

    private Integer reviewCount;

    private Integer penaltyPoints = 0;

    @Column(nullable = false)
    private boolean emailVerified = false;
}
