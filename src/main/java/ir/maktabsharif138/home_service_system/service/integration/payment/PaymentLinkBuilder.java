package ir.maktabsharif138.home_service_system.service.integration.payment;

import org.springframework.stereotype.Component;

@Component
public class PaymentLinkBuilder {

    private static final String BASE_URL = "http://localhost:8080/payment";

    public String build(String paymentReference) {

        return BASE_URL + "/" + paymentReference;
    }
}