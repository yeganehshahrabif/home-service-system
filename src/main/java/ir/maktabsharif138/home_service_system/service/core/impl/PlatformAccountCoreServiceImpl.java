package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.PlatformAccount;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.PlatformAccountRepository;
import ir.maktabsharif138.home_service_system.service.core.PlatformAccountCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlatformAccountCoreServiceImpl implements PlatformAccountCoreService {

    private final PlatformAccountRepository platformAccountRepository;

    @Override
    public PlatformAccount getMainAccount() {

        return platformAccountRepository.findTopByOrderByIdAsc()
                .orElseThrow(() ->
                        new NotFoundException(
                                "PLATFORM_ACCOUNT_NOT_FOUND"
                        ));
    }
}
