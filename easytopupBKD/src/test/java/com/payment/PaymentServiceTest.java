package com.payment;
import com.fasterxml.jackson.databind.JsonNode;
import com.payment.gateaway.model.PaymentRequest;
import com.payment.gateaway.service.PaymentService;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.RequestBodyEntity;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    private final String url = "https://api.sandbox.hit-pay.com/v1/payment-requests";
    private final String apiKey = "dummy-api-key";
    private final String redirectUrl = "https://example.com/redirect";
    private final String webhookUrl = "https://example.com/webhook";

    @Test
    void testPaymentRequestApi_Success() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(10.0);
        request.setCurrency("SGD");

        String mockResponse = """
        {
          "id": "9f1e46b8-6372-41b9-a7e8-051af9f6afdb",
          "status": "pending"
        }
        """;
        ReflectionTestUtils.setField(paymentService, "url", url);
        ReflectionTestUtils.setField(paymentService, "apiKey", apiKey);
        ReflectionTestUtils.setField(paymentService, "redirectUrl", redirectUrl);
        ReflectionTestUtils.setField(paymentService, "webhookUrl", webhookUrl);

        HttpRequestWithBody mockRequest = Mockito.mock(HttpRequestWithBody.class);
        HttpRequestWithBody mockHeader = Mockito.mock(HttpRequestWithBody.class);
        RequestBodyEntity mockBodyEntity = Mockito.mock(RequestBodyEntity.class);
        HttpResponse<String> mockHttpResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockHttpResponse.getBody()).thenReturn(mockResponse);

        try (MockedStatic<Unirest> unirest = Mockito.mockStatic(Unirest.class)) {
            unirest.when(() -> Unirest.post(url)).thenReturn(mockRequest);

            Mockito.when(mockRequest.header(Mockito.anyString(), Mockito.anyString())).thenReturn(mockHeader);
            Mockito.when(mockHeader.header(Mockito.anyString(), Mockito.anyString())).thenReturn(mockHeader);
            Mockito.when(mockHeader.body(Mockito.anyString())).thenReturn(mockBodyEntity);
            Mockito.when(mockBodyEntity.asString()).thenReturn(mockHttpResponse);

            JsonNode response = paymentService.paymentRequestApi(request);
            assertNotNull(response);
            assertEquals("9f1e46b8-6372-41b9-a7e8-051af9f6afdb", response.get("id").asText());
        }
    }

    @Test
    void testPaymentRequestApi_Failure() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(10.0);
        request.setCurrency("SGD");

        ReflectionTestUtils.setField(paymentService, "url", url);
        ReflectionTestUtils.setField(paymentService, "apiKey", apiKey);
        ReflectionTestUtils.setField(paymentService, "redirectUrl", redirectUrl);
        ReflectionTestUtils.setField(paymentService, "webhookUrl", webhookUrl);

        HttpRequestWithBody mockRequest = Mockito.mock(HttpRequestWithBody.class);
        HttpRequestWithBody mockHeader = Mockito.mock(HttpRequestWithBody.class);
        RequestBodyEntity mockBodyEntity = Mockito.mock(RequestBodyEntity.class);

        try (MockedStatic<Unirest> unirest = Mockito.mockStatic(Unirest.class)) {
            unirest.when(() -> Unirest.post(url)).thenReturn(mockRequest);

            Mockito.when(mockRequest.header(Mockito.anyString(), Mockito.anyString())).thenReturn(mockHeader);
            Mockito.when(mockHeader.header(Mockito.anyString(), Mockito.anyString())).thenReturn(mockHeader);
            Mockito.when(mockHeader.body(Mockito.anyString())).thenReturn(mockBodyEntity);
            Mockito.when(mockBodyEntity.asString()).thenThrow(new RuntimeException("Simulated error"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                paymentService.paymentRequestApi(request);
            });

            assertEquals("Simulated error", exception.getMessage());
        }
    }
}
