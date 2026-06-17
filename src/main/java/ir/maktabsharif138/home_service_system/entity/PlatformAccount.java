package ir.maktabsharif138.home_service_system.entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlatformAccount extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL)
    private Wallet wallet;
}