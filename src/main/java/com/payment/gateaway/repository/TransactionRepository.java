package com.payment.gateaway.repository;

import com.payment.gateaway.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Optional<Transaction> findByTransactionId(String payment_request_id);

    Transaction findByPhoneNumberAndPaymentRequestId(String mobile, String paymentRequestId);

}
