package ir.maktabsharif138.home_service_system.bootstrap;

import ir.maktabsharif138.home_service_system.entity.PlatformAccount;
import ir.maktabsharif138.home_service_system.entity.Wallet;
import ir.maktabsharif138.home_service_system.repository.PlatformAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PlatformBootstrap implements ApplicationRunner {

    private final PlatformAccountRepository repository;

    @Override
    public void run(ApplicationArguments args) {

        if (repository.count() > 0) {
            return;
        }

        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);

        PlatformAccount account = new PlatformAccount();
        account.setWallet(wallet);

        repository.save(account);
    }
}