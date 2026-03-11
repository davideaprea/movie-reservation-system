package com.mrs.app.payment.service;

import com.mrs.app.payment.dto.PaymentUpdateRequest;
import com.mrs.app.payment.dto.PaymentResponse;
import com.mrs.app.payment.dto.PaymentCreateRequest;
import com.mrs.app.payment.entity.Payment;
import com.mrs.app.payment.enumeration.PaymentStatus;
import com.mrs.app.payment.mapper.PayPalOrderMapper;
import com.mrs.app.payment.mapper.PaymentMapper;
import com.mrs.app.payment.repository.PaymentDAO;
import com.mrs.app.shared.exception.DomainRequirementError;
import com.mrs.app.shared.exception.DomainRequirementException;
import com.mrs.app.shared.exception.EntityNotFondException;
import com.mrs.app.shared.exception.EntityNotFoundError;
import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.models.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PaymentService {
    private final PaypalServerSdkClient paymentGateway;
    private final PaymentDAO paymentDAO;
    private final PaymentMapper paymentMapper;
    private final PayPalOrderMapper payPalOrderMapper;

    public PaymentResponse create(PaymentCreateRequest createRequest) {
        Order createdOrder = paymentGateway
                .getOrdersController()
                .createOrder(payPalOrderMapper.toCreateOrderInput(createRequest))
                .getResult();
        Payment paymentToSave = paymentMapper.toEntity(createRequest, createdOrder);
        Payment savedPayment = paymentDAO.save(paymentToSave);

        return paymentMapper.toResponse(savedPayment);
    }

    public PaymentResponse complete(PaymentUpdateRequest completionRequest) {
        Payment pendingPayment = paymentDAO
                .findByIdAndUserId(completionRequest.paymentId(), completionRequest.userId())
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Payment.class.getSimpleName(),
                        completionRequest
                )));

        if (!PaymentStatus.PENDING.equals(pendingPayment.getStatus())) {
            throw new DomainRequirementException(new DomainRequirementError(
                    "The selected payment is already closed.",
                    PaymentUpdateRequest.Fields.paymentId
            ));
        }

        Order capturedOrder = paymentGateway.getOrdersController().captureOrder(new CaptureOrderInput
                .Builder()
                .id(pendingPayment.getGatewayOrder().getId())
                .build()).getResult();
        String captureId = Optional.ofNullable(capturedOrder.getPurchaseUnits())
                .map(List::getFirst)
                .map(PurchaseUnit::getPayments)
                .map(PaymentCollection::getCaptures)
                .map(List::getFirst)
                .map(OrdersCapture::getId)
                .orElseThrow(() -> new NoSuchElementException("The PayPal gateway hasn't returned the expected capture id."));

        pendingPayment.setStatus(PaymentStatus.COMPLETED);
        pendingPayment.getGatewayOrder().setCompletionId(captureId);

        return paymentMapper.toResponse(paymentDAO.save(pendingPayment));
    }

    public PaymentResponse refund(PaymentUpdateRequest updateRequest) {
        Payment payment = paymentDAO
                .findByIdAndUserId(updateRequest.paymentId(), updateRequest.userId())
                .orElseThrow(() -> new EntityNotFondException(new EntityNotFoundError(
                        Payment.class.getSimpleName(),
                        updateRequest
                )));

        if (!PaymentStatus.COMPLETED.equals(payment.getStatus())) {
            throw new DomainRequirementException(new DomainRequirementError(
                    "The selected payment is not completed.",
                    PaymentUpdateRequest.Fields.paymentId
            ));
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentGateway.getPaymentsController().refundCapturedPayment(new RefundCapturedPaymentInput.Builder()
                .captureId(payment.getGatewayOrder().getCompletionId())
                .build());

        return paymentMapper.toResponse(paymentDAO.save(payment));
    }
}
