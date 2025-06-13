package com.payment.gateaway.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.payment.gateaway.exception.PaymentException;
import com.payment.gateaway.model.PaymentRequest;
import com.payment.gateaway.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/payment")
public class HitpayController {

    private static final Logger logger = LoggerFactory.getLogger(HitpayController.class);

    @Autowired
    private PaymentService paymentService;


    @PostMapping("/request")
    public ResponseEntity<JsonNode> paymentRequest(@RequestBody PaymentRequest paymentRequest){
        logger.info("=>Requested to payment checkout<=");
        try {
            JsonNode result = paymentService.paymentRequestApi(paymentRequest);
            return ResponseEntity.ok(result);
        }
        catch (PaymentException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getErrorBody());
        }catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
