package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.dto.request.UserSearchRequest;
import ir.maktabsharif138.home_service_system.entity.BaseUser;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.service.core.UserSearchCoreService;
import ir.maktabsharif138.home_service_system.service.core.strategy.UserSearchStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserSearchCoreServiceImpl implements UserSearchCoreService {

    private final List<UserSearchStrategy> strategies;

    @Override
    public Page<? extends BaseUser> search(UserSearchRequest request, Pageable pageable) {

        if (hasRole(request)) {
            return searchByRole(request, pageable);
        }


        return searchBoth(request, pageable);
    }


    private Page<? extends BaseUser> searchByRole(UserSearchRequest request, Pageable pageable) {

        return resolveStrategy(request.getRole())
                .search(request, pageable);
    }

    private Page<? extends BaseUser> searchBoth(UserSearchRequest request, Pageable pageable) {

        Page<Expert> experts = searchExperts(request, pageable);
        if (hasExpertOnlyFilters(request)) {
            return experts;
        }

        Page<Customer> customers = searchCustomers(request, pageable);

        return mergePages(experts, customers, pageable);
    }

    private boolean hasExpertOnlyFilters(UserSearchRequest request) {
        return Objects.nonNull(request.getHomeServiceId())
                || Objects.nonNull(request.getMinRating())
                || Objects.nonNull(request.getMaxRating());
    }

    private Page<BaseUser> mergePages(
            Page<Expert> experts,
            Page<Customer> customers,
            Pageable pageable
    ) {

        List<BaseUser> content = Stream.concat(
                experts.getContent().stream(),
                customers.getContent().stream()
        ).toList();

        long total = experts.getTotalElements() + customers.getTotalElements();

        return new PageImpl<>(content, pageable, total);
    }


    private Page<Expert> searchExperts(UserSearchRequest request, Pageable pageable) {

        return resolveStrategy(Role.EXPERT)
                .search(request, pageable)
                .map(Expert.class::cast);
    }


    private Page<Customer> searchCustomers(UserSearchRequest request, Pageable pageable) {

        return resolveStrategy(Role.CUSTOMER)
                .search(request, pageable)
                .map(Customer.class::cast);
    }


    private UserSearchStrategy resolveStrategy(Role role) {

        return strategies.stream()
                .filter(Objects::nonNull)
                .filter(s -> s.supports(role))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No strategy found for role: " + role
                        )
                );
    }


    private boolean hasRole(UserSearchRequest request) {
        return Objects.nonNull(request.getRole());
    }
}