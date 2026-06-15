package ir.maktabsharif138.home_service_system.repository;

import ir.maktabsharif138.home_service_system.entity.Customer;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<@NonNull Customer,@NonNull Long>,
        JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

}
