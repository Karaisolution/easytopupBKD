package com.payment.gateaway.controller;

import com.payment.gateaway.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;


    @GetMapping("/transaction")
    public ResponseEntity<?> getTransaction(@RequestParam String mobile,@RequestParam String paymentRequestId){
            logger.info(" => Request to get transaction <=");
            return transactionService.getTransaction(mobile,paymentRequestId);
    }
}
