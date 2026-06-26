package ir.maktabsharif138.home_service_system.service.core.strategy;

import ir.maktabsharif138.home_service_system.dto.request.OrderHistoryFilterRequest;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryStrategy {

    boolean supports(Role role);

    Page<CustomerOrder> getHistory(
            Long userId,
            OrderHistoryFilterRequest request,
            Pageable pageable
    );
}