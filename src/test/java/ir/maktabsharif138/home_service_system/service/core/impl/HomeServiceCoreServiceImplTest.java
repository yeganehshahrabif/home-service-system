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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HomeServiceCoreServiceImplTest {

    @Mock
    private HomeServiceRepository homeServiceRepository;

    @Mock
    private ExpertCoreService expertCoreService;

    @Mock
    private ExpertRepository expertRepository;

    @InjectMocks
    private HomeServiceCoreServiceImpl service;

    private HomeService homeService;
    private Expert expert;

    @BeforeEach
    void setUp() {
        homeService = new HomeService();
        homeService.setId(1L);
        homeService.setName("Cleaning");

        expert = new Expert();
        expert.setId(1L);
        expert.setHomeServices(new HashSet<>());
    }

    @Test
    void create_shouldSaveService() {

        when(homeServiceRepository.existsByNameAndParentServiceId(
                "Cleaning", null))
                .thenReturn(false);

        when(homeServiceRepository.save(homeService))
                .thenReturn(homeService);

        HomeService result = service.create(homeService);

        assertEquals(homeService, result);
        verify(homeServiceRepository).save(homeService);
    }

    @Test
    void create_shouldThrow_whenDuplicateExists() {

        when(homeServiceRepository.existsByNameAndParentServiceId(
                "Cleaning", null))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> service.create(homeService)
        );
    }

    @Test
    void create_shouldSave_whenParentExists() {

        HomeService parent = new HomeService();
        parent.setId(10L);

        homeService.setParentService(parent);

        when(homeServiceRepository.existsByNameAndParentServiceId(
                "Cleaning",
                10L))
                .thenReturn(false);

        when(homeServiceRepository.save(homeService))
                .thenReturn(homeService);

        service.create(homeService);

        verify(homeServiceRepository)
                .existsByNameAndParentServiceId(
                        "Cleaning",
                        10L
                );
    }

    @Test
    void checkUpdate_shouldThrow_whenParentIsSelf() {

        HomeServiceUpdateRequest request =
                new HomeServiceUpdateRequest();

        request.setParentServiceId(1L);

        assertThrows(
                BadRequestException.class,
                () -> service.checkUpdate(homeService, request)
        );
    }

    @Test
    void checkUpdate_shouldPass_whenParentIdIsNull() {

        HomeServiceUpdateRequest request =
                new HomeServiceUpdateRequest();

        request.setName("Cleaning");

        when(homeServiceRepository.findByNameAndParentServiceId(
                "Cleaning", null))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(
                () -> service.checkUpdate(homeService, request)
        );
    }

    @Test
    void checkUpdate_shouldUseExistingName_whenRequestNameIsBlank() {

        HomeServiceUpdateRequest request =
                new HomeServiceUpdateRequest();

        request.setName("");

        when(homeServiceRepository.findByNameAndParentServiceId(
                "Cleaning", null))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(
                () -> service.checkUpdate(homeService, request)
        );

        verify(homeServiceRepository)
                .findByNameAndParentServiceId(
                        "Cleaning",
                        null
                );
    }

    @Test
    void checkUpdate_shouldUseExistingParent_whenRequestParentNull() {

        HomeService parent = new HomeService();
        parent.setId(10L);

        homeService.setParentService(parent);

        HomeServiceUpdateRequest request =
                new HomeServiceUpdateRequest();

        request.setName("Cleaning");

        when(homeServiceRepository.findByNameAndParentServiceId(
                "Cleaning",
                10L))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(
                () -> service.checkUpdate(homeService, request)
        );
    }

    @Test
    void checkUpdate_shouldThrow_whenDuplicateServiceExists() {

        HomeServiceUpdateRequest request =
                new HomeServiceUpdateRequest();

        request.setName("Cleaning");

        HomeService duplicate = new HomeService();
        duplicate.setId(99L);

        when(homeServiceRepository.findByNameAndParentServiceId(
                "Cleaning",
                null))
                .thenReturn(Optional.of(duplicate));

        assertThrows(
                DuplicateResourceException.class,
                () -> service.checkUpdate(homeService, request)
        );
    }

    @Test
    void checkUpdate_shouldNotThrow_whenFoundServiceIsSameEntity() {

        HomeServiceUpdateRequest request =
                new HomeServiceUpdateRequest();

        request.setName("Cleaning");

        when(homeServiceRepository.findByNameAndParentServiceId(
                "Cleaning",
                null))
                .thenReturn(Optional.of(homeService));

        assertDoesNotThrow(
                () -> service.checkUpdate(homeService, request)
        );
    }

    @Test
    void update_shouldSaveService() {

        when(homeServiceRepository.save(homeService))
                .thenReturn(homeService);

        HomeService result = service.update(homeService);

        assertEquals(homeService, result);
    }

    @Test
    void delete_shouldDeleteService() {

        homeService.setSubServices(new HashSet<>());
        homeService.setExperts(new HashSet<>());

        when(homeServiceRepository.findWithSubServicesById(1L))
                .thenReturn(Optional.of(homeService));

        service.delete(1L);

        verify(homeServiceRepository).delete(homeService);
    }

    @Test
    void delete_shouldThrow_whenHasSubServices() {

        HomeService child = new HomeService();
        homeService.getSubServices().add(child);

        when(homeServiceRepository.findWithSubServicesById(1L))
                .thenReturn(Optional.of(homeService));

        assertThrows(
                BadRequestException.class,
                () -> service.delete(1L)
        );
    }

    @Test
    void delete_shouldThrow_whenAssignedToExperts() {

        homeService.setSubServices(new HashSet<>());
        homeService.getExperts().add(expert);

        when(homeServiceRepository.findWithSubServicesById(1L))
                .thenReturn(Optional.of(homeService));

        assertThrows(
                BadRequestException.class,
                () -> service.delete(1L)
        );
    }

    @Test
    void findById_shouldReturnService() {

        when(homeServiceRepository.findWithSubServicesById(1L))
                .thenReturn(Optional.of(homeService));

        HomeService result = service.findById(1L);

        assertEquals(homeService, result);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {

        when(homeServiceRepository.findWithSubServicesById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.findById(1L)
        );
    }

    @Test
    void addExpertToSubService_shouldAddSuccessfully() {

        HomeService parent = new HomeService();
        parent.setId(10L);

        homeService.setParentService(parent);
        homeService.setExperts(new HashSet<>());

        when(expertCoreService.findById(1L)).thenReturn(expert);

        when(homeServiceRepository.findWithSubServicesById(1L))
                .thenReturn(Optional.of(homeService));

        service.addExpertToSubService(1L, 1L);

        assertTrue(expert.getHomeServices().contains(homeService));
        verify(expertRepository).save(expert);
    }

    @Test
    void addExpertToSubService_shouldThrow_whenMainService() {

        homeService.setParentService(null);

        when(expertCoreService.findById(1L))
                .thenReturn(expert);

        when(homeServiceRepository.findWithSubServicesById(1L))
                .thenReturn(Optional.of(homeService));

        assertThrows(
                BadRequestException.class,
                () -> service.addExpertToSubService(1L, 1L)
        );
    }

    @Test
    void addExpertToSubService_shouldThrow_whenAlreadyAssigned() {

        HomeService parent = new HomeService();
        homeService.setParentService(parent);

        expert.getHomeServices().add(homeService);

        when(expertCoreService.findById(1L))
                .thenReturn(expert);

        when(homeServiceRepository.findWithSubServicesById(1L))
                .thenReturn(Optional.of(homeService));

        assertThrows(
                BadRequestException.class,
                () -> service.addExpertToSubService(1L, 1L)
        );
    }

    @Test
    void removeExpertFromSubService_shouldRemoveSuccessfully() {

        expert.getHomeServices().add(homeService);
        homeService.getExperts().add(expert);

        when(expertCoreService.findById(1L))
                .thenReturn(expert);

        when(homeServiceRepository.findWithSubServicesById(1L))
                .thenReturn(Optional.of(homeService));

        service.removeExpertFromSubService(1L, 1L);

        assertFalse(expert.getHomeServices().contains(homeService));
        verify(expertRepository).save(expert);
    }

    @Test
    void removeExpertFromSubService_shouldThrow_whenNotAssigned() {

        when(expertCoreService.findById(1L))
                .thenReturn(expert);

        when(homeServiceRepository.findWithSubServicesById(1L))
                .thenReturn(Optional.of(homeService));

        assertThrows(
                BadRequestException.class,
                () -> service.removeExpertFromSubService(1L, 1L)
        );
    }

    @Test
    void findAllMainServices_shouldReturnList() {

        List<HomeService> services = List.of(homeService);

        when(homeServiceRepository.findByParentServiceIsNull())
                .thenReturn(services);

        List<HomeService> result =
                service.findAllMainServices();

        assertEquals(1, result.size());
    }

    @Test
    void findSubServicesByParentId_shouldReturnList() {

        List<HomeService> services = List.of(homeService);

        when(homeServiceRepository.findWithSubServicesById(1L))
                .thenReturn(Optional.of(homeService));

        when(homeServiceRepository.findByParentServiceId(1L))
                .thenReturn(services);

        List<HomeService> result =
                service.findSubServicesByParentId(1L);

        assertEquals(1, result.size());
    }
}