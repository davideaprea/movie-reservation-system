package config;

import io.github.davideaprea.payment.component.PaymentGateway;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockPaymentGateway {
    @Bean
    public PaymentGateway paymentGateway() {
        return Mockito.mock(PaymentGateway.class);
    }
}
