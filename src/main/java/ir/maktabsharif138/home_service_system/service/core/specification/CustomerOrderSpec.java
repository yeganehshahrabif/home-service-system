package ir.maktabsharif138.home_service_system.service.core.specification;

import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.List;

public final class CustomerOrderSpec {

    private CustomerOrderSpec() {
    }

    static void addCustomer(
            Long customerId,
            Root<CustomerOrder> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {

        predicates.add(
                cb.equal(
                        root.get("customer").get("id"),
                        customerId
                )
        );
    }

    static void addExpert(
            Long expertId,
            Root<CustomerOrder> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {

        predicates.add(
                cb.equal(
                        root.get("acceptedOffer")
                                .get("expert")
                                .get("id"),
                        expertId
                )
        );
    }

    static void addStatus(
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

    static void addService(
            Long serviceId,
            Root<CustomerOrder> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {

        predicates.add(
                cb.equal(
                        root.get("homeService")
                                .get("id"),
                        serviceId
                )
        );
    }

    static void addFromDate(
            LocalDateTime fromDate,
            Root<CustomerOrder> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {

        predicates.add(
                cb.greaterThanOrEqualTo(
                        root.get("orderDate"),
                        fromDate
                )
        );
    }

    static void addToDate(
            LocalDateTime toDate,
            Root<CustomerOrder> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {

        predicates.add(
                cb.lessThanOrEqualTo(
                        root.get("orderDate"),
                        toDate
                )
        );
    }
}