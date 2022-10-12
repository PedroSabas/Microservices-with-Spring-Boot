package com.pedrovega.PaymentService.service.impl;

import com.pedrovega.PaymentService.entity.TransactionDetails;
import com.pedrovega.PaymentService.model.PaymentRequest;
import com.pedrovega.PaymentService.repository.TransactionDetailsRepository;
import com.pedrovega.PaymentService.service.PaymentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording Payment Details: {}", paymentRequest);

        TransactionDetails transactionDetails
                = TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentStatus("SUCCESS")
                .orderId(paymentRequest.getOrderId())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .build();
        transactionDetailsRepository.save(transactionDetails);

        log.info("Transaction Completed with Id: {}",transactionDetails.getId());

        return transactionDetails.getId();
    }
}
