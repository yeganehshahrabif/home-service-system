package ir.maktabsharif138.home_service_system.security;

import ir.maktabsharif138.home_service_system.entity.BaseUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public SecurityUser getCurrentSecurityUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof SecurityUser securityUser)) {
            throw new IllegalStateException("Authenticated user not found");
        }

        return securityUser;
    }

    public BaseUser getCurrentUser() {
        return getCurrentSecurityUser().getUser();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}