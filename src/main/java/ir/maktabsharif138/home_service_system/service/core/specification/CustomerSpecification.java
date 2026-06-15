package ir.maktabsharif138.home_service_system.service.core.specification;

import ir.maktabsharif138.home_service_system.dto.request.UserSearchRequest;
import ir.maktabsharif138.home_service_system.entity.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.*;
public final class CustomerSpecification {

    private CustomerSpecification() {}

    public static Specification<Customer> filter(UserSearchRequest req) {

        return (root, query, cb) -> {

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            Spec.addIfValid(req.getName(),
                    name -> addName(name, root, cb, predicates)
            );

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void addName(
            String name,
            Root<Customer> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {
        String pattern = "%" + name.toLowerCase().trim() + "%";

        predicates.add(
                cb.or(
                        cb.like(cb.lower(root.get("firstName")), pattern),
                        cb.like(cb.lower(root.get("lastName")), pattern)
                )
        );
    }
}