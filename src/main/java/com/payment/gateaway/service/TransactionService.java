package com.payment.gateaway.service;

import com.payment.gateaway.model.Transaction;
import com.payment.gateaway.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public ResponseEntity<?> getTransaction(String mobile,String paymentRequestId){
        Transaction transaction = transactionRepository.findByPhoneNumberAndPaymentRequestId(mobile,paymentRequestId);
        if (transaction != null) {
            return ResponseEntity.ok(new Transaction(transaction));
        } else {
            return ResponseEntity.status(404).body("Transaction not found");
        }
    }
}
