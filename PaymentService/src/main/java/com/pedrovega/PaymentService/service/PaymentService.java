package com.pedrovega.PaymentService.service;

import com.pedrovega.PaymentService.model.PaymentRequest;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);
}
