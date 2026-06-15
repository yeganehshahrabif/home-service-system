package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.dto.request.UserSearchRequest;
import ir.maktabsharif138.home_service_system.entity.BaseUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserSearchCoreService {

    Page<? extends BaseUser> search(
            UserSearchRequest request,
            Pageable pageable
    );
}
