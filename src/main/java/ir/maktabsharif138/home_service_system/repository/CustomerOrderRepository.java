package ir.maktabsharif138.home_service_system.repository;

import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<@NonNull CustomerOrder, @NonNull Long> {

    boolean existsByAcceptedOffer_Expert_IdAndOrderStatusIn(
            Long expertId,
            Collection<OrderStatus> statuses
    );

    List<CustomerOrder> findByCustomerId(Long customerId);

    @EntityGraph(attributePaths = {
            "offers",
            "offers.expert",
            "customer",
            "homeService"
    })
    List<CustomerOrder> findByOrderStatus(OrderStatus status);

    List<CustomerOrder> findByHomeService_Experts_IdAndOrderStatusIn(
            Long expertId,
            Collection<OrderStatus> statuses
    );
}
