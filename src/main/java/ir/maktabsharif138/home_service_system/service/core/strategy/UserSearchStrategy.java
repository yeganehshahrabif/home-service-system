package ir.maktabsharif138.home_service_system.service.core.strategy;

import ir.maktabsharif138.home_service_system.dto.request.UserSearchRequest;
import ir.maktabsharif138.home_service_system.entity.BaseUser;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserSearchStrategy {

    boolean supports(Role role);

    Page<? extends BaseUser> search(
            UserSearchRequest request,
            Pageable pageable
    );
}