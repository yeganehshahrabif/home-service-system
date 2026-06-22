package ir.maktabsharif138.home_service_system.service.integration.payment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentLinkBuilderTest {

    private final PaymentLinkBuilder builder = new PaymentLinkBuilder();

    @Test
    void build_shouldReturnCorrectPaymentUrl_whenReferenceIsValid() {

        String reference = "abc123";

        String result = builder.build(reference);

        assertEquals(
                "http://localhost:8080/payment/abc123",
                result
        );
    }

    @Test
    void build_shouldStartWithBaseUrl() {

        String result = builder.build("PAY-999");

        assertTrue(result.startsWith("http://localhost:8080/payment/"));
    }

    @Test
    void build_shouldAppendReferenceAtEnd() {

        String reference = "PAY-999";
        String result = builder.build(reference);

        assertTrue(result.endsWith(reference));
    }

    @Test
    void build_shouldNotReturnNull() {

        String result = builder.build("x");

        assertNotNull(result);
    }

    @Test
    void build_shouldHandleEmptyString() {

        String result = builder.build("");

        assertEquals("http://localhost:8080/payment/", result);
    }
}