package com.payment.gateaway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.gateaway.exception.TransactionNotFoundException;
import com.payment.gateaway.model.PaymentRequest;
import com.payment.gateaway.model.Transaction;
import com.payment.gateaway.repository.TransactionRepository;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Value("${hitpay.api.key}")
    public String apiKey;

    @Value("${hitpay.url}")
    public String url;

    @Value("${redirect.url}")
    public String redirectUrl;

    @Value("${webhook.url}")
    public String webhookUrl;
    @Autowired
    private TransactionRepository transactionRepository;

    public void handlePayment(Map<String, Object> payload, Transaction transaction) {

        logger.info("Handling PaymentSuccess event");
        try {
            String transactionId = (String) payload.get("id");
            String customerId = (String) payload.get("customer_id");
            String status = (String) payload.get("status");
            String currency = (String) payload.get("currency");
            double amount = payload.get("amount") instanceof Number
                    ? ((Number) payload.get("amount")).doubleValue()
                    : 0.0;

            Map<String, Object> customer = (Map<String, Object>) payload.get("customer");
            String name = customer != null ? (String) customer.get("name") : null;
            String email = customer != null ? (String) customer.get("email") : null;
            String phoneNumber = customer != null ? (String) customer.get("phone_number") : null;

            Map<String, Object> paymentProvider = (Map<String, Object>) payload.get("payment_provider");
            Map<String, Object> charge = paymentProvider != null ? (Map<String, Object>) paymentProvider.get("charge") : null;
            String method = charge != null ? (String) charge.get("method") : null;

            Map<String, Object> payment_request = (Map<String, Object>) payload.get("payment_request");
            String paymentStatus = payment_request != null ? (String) payment_request.get("status") : null;

            String payment_request_id = (String) payload.get("payment_request_id");

            HttpResponse<String> response = Unirest.get(url+"/"+payment_request_id)
                    .header("X-BUSINESS-API-KEY", apiKey)
                    .asString();
            System.out.println(response.getBody());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());

            JsonNode referenceNode = rootNode.path("reference_number");

            transaction.setTransactionId(transactionId);
            transaction.setCustomerId(customerId);
            transaction.setName(name);
            transaction.setEmail(email);
            transaction.setPhoneNumber(phoneNumber);
            transaction.setCurrency(currency);
            transaction.setAmount(amount);
            transaction.setMethod(method);
            transaction.setPaymentStatus(paymentStatus);
            transaction.setStatus(status);
            transaction.setPaymentRequestId(payment_request_id);
            if (!referenceNode.isMissingNode() && !referenceNode.isNull()) {
                String referenceNumber = referenceNode.asText();
                transaction.setReferenceNumber(referenceNumber);
            } else {
                transaction.setReferenceNumber(null);
            }
            transactionRepository.save(transaction);
        }
        catch (Exception e){
            logger.error("Error mapping webhook payload to Transaction entity: ", e);
        }
    }

    public void handleUpdatePayment(Map<String, Object> payload){
        try{
            String payment_request_id = (String) payload.get("payment_request_id");
            Map<String, Object> payment_request = (Map<String, Object>) payload.get("payment_request");
            String paymentStatus = payment_request != null ? (String) payment_request.get("status") : null;
            Transaction transaction = transactionRepository.findByTransactionId(payment_request_id)
                    .orElseThrow(() -> new TransactionNotFoundException("Transaction record not found: " + payment_request_id));
            transaction.setStatus((String) payload.get("status"));
            transaction.setPaymentStatus(paymentStatus);
            transactionRepository.save(transaction);
        }
        catch (TransactionNotFoundException e) {
            logger.error("Transaction not found: {}", e.getMessage());
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    public JsonNode paymentRequestApi(PaymentRequest paymentRequest){
        ObjectMapper mapper = new ObjectMapper();
        try {
            paymentRequest.setRedirectUrl(redirectUrl);
            paymentRequest.setWebhook(webhookUrl);

            String json = mapper.writeValueAsString(paymentRequest);

            HttpResponse<String> response = Unirest.post(url)
                    .header("X-BUSINESS-API-KEY", apiKey)
                    .header("Content-Type", "application/json")
                    .body(json)
                    .asString();
            return mapper.readTree(response.getBody());
        } catch (JsonProcessingException | UnirestException e) {
                logger.error("Failed to process payment request: {}", e.getMessage());
                throw new RuntimeException("Failed to process payment request");
        }
    }
}
