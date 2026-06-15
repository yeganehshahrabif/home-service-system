package ir.maktabsharif138.home_service_system.service.core.specification;

import ir.maktabsharif138.home_service_system.dto.request.UserSearchRequest;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.HomeService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.*;
public final class ExpertSpecification {

    private ExpertSpecification() {}

    public static Specification<Expert> filter(UserSearchRequest req) {

        return (root, query, cb) -> {

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            Spec.addIfValid(req.getName(),
                    name -> addName(name, root, cb, predicates)
            );

            Spec.addIfValid(req.getHomeServiceId(),
                    id -> addService(id, root, cb, predicates)
            );

            Spec.addIfValid(req.getMinRating(),
                    min -> addMinRating(min, root, cb, predicates)
            );

            Spec.addIfValid(req.getMaxRating(),
                    max -> addMaxRating(max, root, cb, predicates)
            );

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void addName(
            String name,
            Root<Expert> root,
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

    private static void addService(
            Long id,
            Root<Expert> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {
        Join<Expert, HomeService> join = root.join("homeServices");

        predicates.add(
                cb.equal(join.get("id"), id)
        );
    }

    private static void addMinRating(
            Double min,
            Root<Expert> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {
        predicates.add(
                cb.greaterThanOrEqualTo(root.get("rating"), min)
        );
    }

    private static void addMaxRating(
            Double max,
            Root<Expert> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {
        predicates.add(
                cb.lessThanOrEqualTo(root.get("rating"), max)
        );
    }
}