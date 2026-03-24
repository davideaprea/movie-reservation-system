package factory;

import com.mrs.app.payment.entity.Intent;

import java.math.BigDecimal;

public class PaymentFactory {
    private PaymentFactory() {
    }

    public static Intent create() {
        return new Intent(null, "order-id", BigDecimal.valueOf(5));
    }
}
