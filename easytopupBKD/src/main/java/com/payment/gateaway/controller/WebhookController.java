package com.payment.gateaway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.gateaway.model.Transaction;
import com.payment.gateaway.service.PaymentService;
import com.payment.gateaway.service.SignatureVerificationService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Value("${hitpay.webhook.salt}")
    private String hitpaySaltKey;

    @Autowired
    private SignatureVerificationService service;

    @Autowired
    private PaymentService paymentService;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
    }

    @PostMapping
    public ResponseEntity<String> handleWebhook(HttpServletRequest request,Transaction transaction) throws IOException {
        logger.info("=>Requested to webhook listening <=");
        String ct = request.getContentType();
        String signatureHeader = request.getHeader("hitpay-signature");
        String eventType = request.getHeader("Hitpay-Event-Type");
        String eventObject = request.getHeader("Hitpay-Event-Object");

        logger.info("Webhook received | content-type={}, event-type={}, sig={}",
                ct, eventType, signatureHeader != null ? "[yes]" : "[no]");

        if (!"application/json".equalsIgnoreCase(ct)
                || signatureHeader == null
                || eventType == null) {
            return ResponseEntity.ok("Ignored");
        }

        String rawBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        if (!service.isValidSignature(rawBody, signatureHeader, hitpaySaltKey)) {
            return ResponseEntity.status(401).body("Invalid Signature");
        }

        Map<String,Object> payload = objectMapper.readValue(rawBody, Map.class);
        String event = eventObject + "." + eventType;
        handleEvent(event, payload,transaction);
        return ResponseEntity.ok("Webhook Received");
    }
    private void handlePayment(Map<String, Object> payload,Transaction transaction) {
        logger.info("Requested to handlePayment");
        paymentService.handlePayment(payload,transaction);
    }

    private void handlePaymentUpdate(Map<String, Object> payload) {
        logger.info("Requested handlePaymentUpdate");
        paymentService.handleUpdatePayment(payload);
    }

    private void handleEvent(String event, Map<String, Object> payload,Transaction transaction) {
        switch (event) {
            case "charge.created":
                handleChargeCreated(payload,transaction);
                break;
            case "charge.updated":
                handleChargeUpdated(payload);
                break;
            default:
                logger.warn("Unhandled event: {}", event);
                break;
        }
    }
    private void handleChargeCreated(Map<String, Object> payload,Transaction transaction) {
        logger.info("Handling charge.created event");
        handlePayment(payload,transaction);
    }

    private void handleChargeUpdated(Map<String, Object> payload) {
        logger.info("Handling charge.updated event");
        handlePaymentUpdate(payload);
    }
}
