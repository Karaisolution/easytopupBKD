package com.payment.gateaway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;


@Service
public class SignatureVerificationService {

    private static final Logger logger = LoggerFactory.getLogger(SignatureVerificationService.class);

    public boolean isValidSignature(String rawBody, String receivedSignature, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(keySpec);
            byte[] hashBytes = sha256_HMAC.doFinal(rawBody.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            String computedSignature = hexString.toString();

            return computedSignature.equals(receivedSignature);
        } catch (Exception e) {
            logger.error("Signature verification failed:{} ", e.getMessage());
            return false;
        }
    }
}
