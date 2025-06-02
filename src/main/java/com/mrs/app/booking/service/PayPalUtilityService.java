package com.mrs.app.booking.service;

import com.mrs.app.booking.dto.internal.PayPalCapturedOrder;
import org.springframework.stereotype.Service;

@Service
public class PayPalUtilityService {
    public String extractCaptureId(PayPalCapturedOrder payPalCapturedOrder) {
        return payPalCapturedOrder
                .purchaseUnits()
                .getFirst()
                .payments()
                .captures()
                .getFirst()
                .id();
    }
}
