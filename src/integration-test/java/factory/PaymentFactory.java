package factory;

import com.mrs.app.payment.entity.Intent;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class PaymentFactory {
    private PaymentFactory() {
    }

    public static Intent create(String orderId, Duration paymentTimeout) {
        LocalDateTime now = LocalDateTime.now();

        return Intent.builder()
                .createdAt(now)
                .expiresAt(now.plus(paymentTimeout))
                .amount(BigDecimal.valueOf(5))
                .orderId(orderId)
                .build();
    }
}
