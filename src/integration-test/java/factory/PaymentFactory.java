package factory;

import com.mrs.app.payment.entity.Intent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentFactory {
    public static Intent create(Duration paymentTimeout) {
        LocalDateTime now = LocalDateTime.now();

        return Intent.builder()
                .createdAt(now)
                .expiresAt(now.plus(paymentTimeout))
                .amount(BigDecimal.valueOf(5))
                .build();
    }
}
