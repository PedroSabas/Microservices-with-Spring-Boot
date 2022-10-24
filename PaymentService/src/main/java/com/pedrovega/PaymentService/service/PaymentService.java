package com.pedrovega.PaymentService.service;

import com.pedrovega.PaymentService.model.PaymentRequest;
import com.pedrovega.PaymentService.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse doPaymentDetailsByOrderId(String orderId);
}
