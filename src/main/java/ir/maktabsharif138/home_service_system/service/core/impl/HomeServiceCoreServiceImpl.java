package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.dto.request.HomeServiceUpdateRequest;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.HomeService;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.DuplicateResourceException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.ExpertRepository;
import ir.maktabsharif138.home_service_system.repository.HomeServiceRepository;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import ir.maktabsharif138.home_service_system.service.core.HomeServiceCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HomeServiceCoreServiceImpl implements HomeServiceCoreService {

    private final HomeServiceRepository homeServiceRepository;
    private final ExpertCoreService expertCoreService;
    private final ExpertRepository expertRepository;

    @Override
    @Transactional
    public HomeService create(HomeService service) {
        Long parentId = Objects.nonNull(service.getParentService())
                ? service.getParentService().getId()
                : null;

        if(homeServiceRepository.existsByNameAndParentServiceId(service.getName(),parentId)) {
            throw new DuplicateResourceException("Service name already exists in this level");
        }
        return homeServiceRepository.save(service);
    }

    @Override
    public void checkUpdate(HomeService existing, HomeServiceUpdateRequest request) {
        checkParentService(existing, request);
        checkDuplicateService(existing, request);

    }

    @Override
    @Transactional
    public HomeService update(HomeService service) {
       return homeServiceRepository.save(service);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        HomeService service = findById(id);
        if(!service.getSubServices().isEmpty()) {
            throw new BadRequestException("Service has subservices");
        }
        if(!service.getExperts().isEmpty()) {
            throw new BadRequestException("Cannot delete service assigned to experts");
        }
        homeServiceRepository.delete(service);
    }

    @Override
    public HomeService findById(Long id) {
        return homeServiceRepository.findById(id).orElseThrow(()
                        -> new NotFoundException("Service not found"));
    }

    @Override
    @Transactional
    public void addExpertToSubService(Long expertId, Long subServiceId) {

        Expert expert = expertCoreService.findById(expertId);
        HomeService subService = findById(subServiceId);

        if (Objects.isNull(subService.getParentService())) {
            throw new BadRequestException("Expert can only be assigned to sub services");
        }
        if (expert.getHomeServices().contains(subService)) {
            throw new BadRequestException("Expert already assigned");
        }

        expert.getHomeServices().add(subService);
        subService.getExperts().add(expert);
        expertRepository.save(expert);

    }

    @Override
    @Transactional
    public void removeExpertFromSubService(Long expertId, Long subServiceId) {

        Expert expert = expertCoreService.findById(expertId);

        HomeService subService = findById(subServiceId);
        if (!expert.getHomeServices().contains(subService)) {
            throw new BadRequestException("Expert is not assigned");
        }

        expert.getHomeServices().remove(subService);
        subService.getExperts().remove(expert);
        expertRepository.save(expert);
    }

    @Override
    public List<HomeService> findAllMainServices() {
        return homeServiceRepository
                .findByParentServiceIsNull();
    }

    @Override
    public List<HomeService> findSubServicesByParentId(Long parentId) {
        findById(parentId);
        return homeServiceRepository
                .findByParentServiceId(parentId);
    }

    private void checkParentService(HomeService existing,
            HomeServiceUpdateRequest request) {

        if (Objects.isNull(request.getParentServiceId())) {
            return;
        }

        if (existing.getId().equals(request.getParentServiceId())) {
            throw new BadRequestException(
                    "Service cannot be parent of itself"
            );
        }
        findById(request.getParentServiceId());
    }

    private void checkDuplicateService(
            HomeService existing,
            HomeServiceUpdateRequest request
    ) {

        String serviceName =
                StringUtils.hasText(request.getName())
                        ? request.getName()
                        : existing.getName();

        Long parentServiceId =
                Objects.nonNull(request.getParentServiceId())
                        ? request.getParentServiceId()
                        : Objects.nonNull(existing.getParentService())
                        ? existing.getParentService().getId()
                        : null;

        homeServiceRepository
                .findByNameAndParentServiceId(
                        serviceName,
                        parentServiceId
                )
                .filter(service ->
                        !service.getId().equals(existing.getId())
                )
                .ifPresent(service -> {
                    throw new DuplicateResourceException(
                            "Service name already exists in this level"
                    );
                });
    }
}
