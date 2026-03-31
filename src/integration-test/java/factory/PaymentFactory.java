package factory;

import com.mrs.app.payment.entity.Payment;

import java.math.BigDecimal;

public class PaymentFactory {
    private PaymentFactory() {
    }

    public static Payment create() {
        return new Payment(null, "order-id", BigDecimal.valueOf(5));
    }
}
