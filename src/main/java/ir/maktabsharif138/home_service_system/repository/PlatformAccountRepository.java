package ir.maktabsharif138.home_service_system.repository;

import ir.maktabsharif138.home_service_system.entity.PlatformAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformAccountRepository extends JpaRepository<PlatformAccount, Long> {

    Optional<PlatformAccount> findTopBy();
}