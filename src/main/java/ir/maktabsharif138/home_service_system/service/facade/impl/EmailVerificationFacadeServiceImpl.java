package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.EmailVerificationToken;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.CustomerRepository;
import ir.maktabsharif138.home_service_system.repository.ExpertRepository;
import ir.maktabsharif138.home_service_system.service.core.EmailVerificationTokenCoreService;
import ir.maktabsharif138.home_service_system.service.facade.EmailVerificationFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailVerificationFacadeServiceImpl
        implements EmailVerificationFacadeService {

    private final EmailVerificationTokenCoreService tokenCoreService;

    private final CustomerRepository customerRepository;

    private final ExpertRepository expertRepository;

    @Override
    @Transactional
    public void verify(String tokenValue) {

        EmailVerificationToken token = tokenCoreService.findValidToken(tokenValue);

        switch (token.getRole()) {

            case CUSTOMER -> verifyCustomer(token);

            case EXPERT -> verifyExpert(token);

            default -> throw new BadRequestException("Invalid role");
        }

        tokenCoreService.markAsUsed(token);
    }

    private void verifyCustomer(EmailVerificationToken token) {

        Customer customer =
                customerRepository
                        .findByEmail(token.getEmail())
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "Customer not found"
                                )
                        );

        customer.setEmailVerified(true);
        customerRepository.save(customer);
    }

    private void verifyExpert(EmailVerificationToken token) {

        Expert expert =
                expertRepository
                        .findByEmail(token.getEmail())
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "Expert not found"
                                )
                        );

        expert.setEmailVerified(true);
        expertRepository.save(expert);
    }
}