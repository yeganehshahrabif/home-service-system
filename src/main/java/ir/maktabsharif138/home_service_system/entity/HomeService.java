package ir.maktabsharif138.home_service_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "home_services_table")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HomeService extends BaseEntity<Long>{

    private static final String PARENT_SERVICE_COLUMN = "parent_service_column";

    @Column(nullable = false)
    private String name;

    private String description;

    private Double basePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PARENT_SERVICE_COLUMN)
    private HomeService parentService;

    @OneToMany(mappedBy = "parentService", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<HomeService> subServices = new HashSet<>();

    @ManyToMany(mappedBy = "homeServices", fetch = FetchType.LAZY)
    private Set<Expert> experts = new HashSet<>();

}
