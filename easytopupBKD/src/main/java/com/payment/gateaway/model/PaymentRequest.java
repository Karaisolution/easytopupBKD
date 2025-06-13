package com.payment.gateaway.model;
import com.fasterxml.jackson.annotation.JsonProperty;


public class PaymentRequest {
    private String email;
    private String name;
    private String phone;
    private Double amount;
    private String currency;
    private String purpose;

    public ExpiryOption getExpires_after() {
        return expires_after;
    }

    public void setExpires_after(ExpiryOption expires_after) {
        this.expires_after = expires_after;
    }

    private ExpiryOption expires_after = ExpiryOption.FIVE_MINUTES;

    public SendOption getSend_email() {
        return send_email;
    }

    public void setSend_email(SendOption send_email) {
        this.send_email = send_email;
    }

    private SendOption send_email = SendOption.TRUE;

    public SendOption getSend_sms() {
        return send_sms;
    }

    public void setSend_sms(SendOption send_sms) {
        this.send_sms = send_sms;
    }

    private SendOption send_sms = SendOption.TRUE;

    public String[] getPayment_methods() {
        return payment_methods;
    }

    public void setPayment_methods(String[] payment_methods) {
        this.payment_methods = payment_methods;
    }

    private String[] payment_methods;


    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @JsonProperty("reference_number")
    private String referenceNumber;


    @JsonProperty("redirect_url")
    private String redirectUrl;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    private String webhook;

}
