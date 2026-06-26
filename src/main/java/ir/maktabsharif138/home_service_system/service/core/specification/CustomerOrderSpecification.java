package ir.maktabsharif138.home_service_system.service.core.specification;

import ir.maktabsharif138.home_service_system.dto.request.OrderHistoryFilterRequest;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.service.core.specification.Spec;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class CustomerOrderSpecification {

    private CustomerOrderSpecification() {
    }

    public static Specification<CustomerOrder> history(Long customerId, OrderHistoryFilterRequest request) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            addCustomer(customerId, root, cb, predicates);

            Spec.addIfValid(
                    request.getStatus(),
                    s -> addStatus(s, root, cb, predicates)
            );

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void addCustomer(
            Long customerId,
            Root<CustomerOrder> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {

        predicates.add(
                cb.equal(root.get("customer").get("id"),
                        customerId
                )
        );
    }

    private static void addStatus(
            OrderStatus status,
            Root<CustomerOrder> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {

        predicates.add(
                cb.equal(
                        root.get("orderStatus"),
                        status
                )
        );
    }
}