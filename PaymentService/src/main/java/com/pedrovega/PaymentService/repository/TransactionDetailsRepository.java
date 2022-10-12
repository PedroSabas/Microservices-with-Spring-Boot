package com.pedrovega.PaymentService.repository;

import com.pedrovega.PaymentService.entity.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailsRepository extends IGenericRepository<TransactionDetails, Long> {

}
