package ir.maktabsharif138.home_service_system.service.core.strategy;

import ir.maktabsharif138.home_service_system.dto.request.UserSearchRequest;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.repository.ExpertRepository;

import ir.maktabsharif138.home_service_system.service.core.specification.ExpertSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpertSearchStrategy implements UserSearchStrategy {

    private final ExpertRepository repository;

    @Override
    public boolean supports(Role role) {
        return Role.EXPERT.equals(role);
    }

    @Override
    public Page<Expert> search(UserSearchRequest request, Pageable pageable) {
        return repository.findAll(
                ExpertSpecification.filter(request),
                pageable
        );

    }
}