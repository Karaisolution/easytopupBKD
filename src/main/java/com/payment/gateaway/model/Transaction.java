package com.payment.gateaway.model;
import jakarta.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "bigint")
    private Long id;

    private String transactionId;
    private String customerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String paymentStatus;
    private String method;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    private String created_at;

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    private String updated_at;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "payment_request_id")
    private String paymentRequestId;

    private String currency;
    private double amount;
    private String status;

    // DEFAULT CONSTRUCTOR - Required by JPA/Hibernate
    public Transaction() {
    }

    // COPY CONSTRUCTOR - Fixed to actually copy values
    public Transaction(Transaction transaction) {
        if (transaction != null) {
            this.id = transaction.getId();
            this.transactionId = transaction.getTransactionId();
            this.created_at = transaction.getCreated_at();
            this.updated_at = transaction.getUpdated_at();
            this.customerId = transaction.getCustomerId();
            this.name = transaction.getName();
            this.email = transaction.getEmail();
            this.phoneNumber = transaction.getPhoneNumber();
            this.paymentStatus = transaction.getPaymentStatus();
            this.method = transaction.getMethod();
            this.referenceNumber = transaction.getReferenceNumber();
            this.paymentRequestId = transaction.getPaymentRequestId();
            this.currency = transaction.getCurrency();
            this.amount = transaction.getAmount();
            this.status = transaction.getStatus();
        }
    }

    // GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getPaymentRequestId() {
        return paymentRequestId;
    }

    public void setPaymentRequestId(String paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
