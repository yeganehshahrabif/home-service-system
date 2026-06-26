package ir.maktabsharif138.home_service_system.service.core.specification;

import ir.maktabsharif138.home_service_system.dto.request.AdminHistoryFilterRequest;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class AdminCustomerHistorySpecification {

    private AdminCustomerHistorySpecification() {
    }

    public static Specification<CustomerOrder> filter(
            Long customerId,
            AdminHistoryFilterRequest request
    ) {

        return (root, query, cb) -> {

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            CustomerOrderSpec.addCustomer(
                    customerId,
                    root,
                    cb,
                    predicates
            );

            applyFilters(
                    request,
                    root,
                    cb,
                    predicates
            );

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void applyFilters(
            AdminHistoryFilterRequest request,
            Root<CustomerOrder> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {

        Spec.addIfValid(
                request.getStatus(),
                status -> CustomerOrderSpec.addStatus(
                        status,
                        root,
                        cb,
                        predicates
                )
        );

        Spec.addIfValid(
                request.getHomeServiceId(),
                serviceId -> CustomerOrderSpec.addService(
                        serviceId,
                        root,
                        cb,
                        predicates
                )
        );

        Spec.addIfValid(
                request.getFromDate(),
                from -> CustomerOrderSpec.addFromDate(
                        from,
                        root,
                        cb,
                        predicates
                )
        );

        Spec.addIfValid(
                request.getToDate(),
                to -> CustomerOrderSpec.addToDate(
                        to,
                        root,
                        cb,
                        predicates
                )
        );
    }
}