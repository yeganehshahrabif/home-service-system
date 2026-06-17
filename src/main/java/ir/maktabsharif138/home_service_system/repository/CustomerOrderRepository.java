package ir.maktabsharif138.home_service_system.repository;

import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends JpaRepository<@NonNull CustomerOrder, @NonNull Long> {

    boolean existsByAcceptedOffer_Expert_IdAndOrderStatusIn(
            Long expertId,
            Collection<OrderStatus> statuses
    );

    Page<CustomerOrder> findByCustomerId(Long customerId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "offers",
            "offers.expert",
            "customer",
            "homeService"
    })
    Page<CustomerOrder> findByOrderStatus(OrderStatus status, Pageable pageable);

    Page<CustomerOrder> findByHomeService_Experts_IdAndOrderStatusIn(
            Long expertId,
            Collection<OrderStatus> statuses,
            Pageable pageable
    );

    @Query("""
                SELECT co
                FROM CustomerOrder co
                WHERE EXISTS (
                    SELECT 1 FROM Offer o
                    WHERE o.customerOrder = co
                    AND o.expert.id = :expertId
                )
            """)
    @EntityGraph(attributePaths = {"customer", "homeService", "acceptedOffer"})
    Page<CustomerOrder> findHistoryByExpertId(Long expertId, Pageable pageable);

    Optional<CustomerOrder> findByIdAndCustomerId(Long orderId, Long customerId);

    boolean existsByIdAndCustomerId(Long orderId, Long customerId);
}
