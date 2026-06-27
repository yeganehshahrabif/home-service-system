package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.response.EmailVerificationResponse;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.EmailVerificationToken;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.service.core.CustomerCoreService;
import ir.maktabsharif138.home_service_system.service.core.EmailVerificationTokenCoreService;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import ir.maktabsharif138.home_service_system.service.facade.EmailVerificationFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailVerificationFacadeServiceImpl
        implements EmailVerificationFacadeService {

    private final EmailVerificationTokenCoreService tokenCoreService;

    private final CustomerCoreService customerCoreService;

    private final ExpertCoreService expertCoreService;

    @Override
    @Transactional
    public EmailVerificationResponse verify(String tokenValue) {

        EmailVerificationToken token = tokenCoreService.findValidToken(tokenValue);

        if (Objects.isNull(token.getRole())) {
            throw new BadRequestException("Invalid role");
        }

        switch (token.getRole()) {

            case CUSTOMER -> verifyCustomer(token);

            case EXPERT -> verifyExpert(token);

            default -> throw new BadRequestException("Invalid role");
        }

        tokenCoreService.markAsUsed(token);
        return new EmailVerificationResponse("Email verified successfully");
    }

    private void verifyCustomer(EmailVerificationToken token) {

        Customer customer = customerCoreService.findByEmail(token.getEmail());
        customerCoreService.verifyEmail(customer);
    }

    private void verifyExpert(EmailVerificationToken token) {

        Expert expert = expertCoreService.findByEmail(token.getEmail());
        expertCoreService.verifyEmail(expert);
    }
}