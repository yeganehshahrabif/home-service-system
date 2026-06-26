package ir.maktabsharif138.home_service_system.service.core.specification;

import ir.maktabsharif138.home_service_system.dto.request.OrderHistoryFilterRequest;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class CustomerOrderSpecification {

    private CustomerOrderSpecification() {
    }

    public static Specification<CustomerOrder> history(
            Long customerId,
            OrderHistoryFilterRequest request
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            CustomerOrderSpec.addCustomer(
                    customerId,
                    root,
                    cb,
                    predicates
            );

            Spec.addIfValid(
                    request.getStatus(),
                    status -> CustomerOrderSpec.addStatus(
                            status,
                            root,
                            cb,
                            predicates
                    )
            );

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}