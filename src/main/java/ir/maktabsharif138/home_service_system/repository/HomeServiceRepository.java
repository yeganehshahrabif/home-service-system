package ir.maktabsharif138.home_service_system.repository;

import ir.maktabsharif138.home_service_system.entity.HomeService;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeServiceRepository extends JpaRepository<@NonNull HomeService,@NonNull Long> {

    boolean existsByNameAndParentServiceId(String name, Long parentHomeServiceId);

    Optional<HomeService> findByName(String name);

    List<HomeService> findByParentServiceIsNull();

    List<HomeService> findByParentServiceId(Long parentId);
}
