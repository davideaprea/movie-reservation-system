package com.mrs.app.payment.util;

import com.paypal.sdk.models.Order;
import com.paypal.sdk.models.OrdersCapture;
import com.paypal.sdk.models.PaymentCollection;
import com.paypal.sdk.models.PurchaseUnit;

import java.util.List;
import java.util.Optional;

public class PayPalOrderUtils {
    private PayPalOrderUtils() {
    }

    public static Optional<String> extractCaptureIdFromOrder(Order order) {
        return Optional.ofNullable(order.getPurchaseUnits())
                .map(List::getFirst)
                .map(PurchaseUnit::getPayments)
                .map(PaymentCollection::getCaptures)
                .map(List::getFirst)
                .map(OrdersCapture::getId);
    }
}
