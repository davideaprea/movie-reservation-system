package com.example.demo.booking.service;

import com.example.demo.booking.dto.OrderDto;
import com.example.demo.booking.response.PayPalOrder;
import com.example.demo.booking.response.PayPalTokenDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.List;

@Service
public class PayPalService {
    private final String clientId;

    private final String clientSecret;

    private final RestClient restClient;

    private final Object lock = new Object();

    private volatile String accessToken;

    private volatile Instant tokenExpiryTime;

    public PayPalService(
            @Value("${paypal.base-url}")
            String baseUrl,

            @Value("${paypal.client-id}")
            String clientId,

            @Value("${paypal.secret}")
            String clientSecret,

            RestClient.Builder builder
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restClient = builder
                .baseUrl(baseUrl)
                .requestInterceptor((request, body, execution) -> {
                    if (!request.getURI().getPath().contains("/v1/oauth2/token")) {
                        refreshAccessToken();

                        request.getHeaders().setBearerAuth(accessToken);
                    }

                    return execution.execute(request, body);
                })
                .build();
    }

    private void refreshAccessToken() {
        if (accessToken != null && isTokenValid()) return;

        synchronized (lock) {
            if (accessToken != null && isTokenValid()) return;

            PayPalTokenDetails tokenDetails = getAccessToken();
            accessToken = tokenDetails.accessToken();
            tokenExpiryTime = Instant.now().plusSeconds(tokenDetails.expiresIn());
        }
    }

    private boolean isTokenValid() {
        return tokenExpiryTime != null && Instant
                .now()
                .isBefore(tokenExpiryTime.minusSeconds(60));
    }

    private PayPalTokenDetails getAccessToken() {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("grant_type", "client_credentials");

        return restClient.post()
                .uri("/v1/oauth2/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(formData)
                .retrieve()
                .body(PayPalTokenDetails.class);
    }

    public PayPalOrder createOrder(OrderDto dto) {
        return restClient.post()
                .uri("/v2/checkout/orders")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public String captureOrder(String orderId) {
        Object response = restClient.post()
                .uri("/v2/checkout/orders/" + orderId + "/capture")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        return getCaptureIdFromResponse(response);
    }

    @SuppressWarnings("unchecked")
    private String getCaptureIdFromResponse(Object response) {
        Map<String, Object> map = (Map<String, Object>) response;

        List<Map<String, Object>> purchaseUnits = (List<Map<String, Object>>) map.get("purchase_units");
        Map<String, Object> firstPurchaseUnit = purchaseUnits.getFirst();

        Map<String, Object> payments = (Map<String, Object>) firstPurchaseUnit.get("payments");
        List<Map<String, Object>> captures = (List<Map<String, Object>>) payments.get("captures");
        Map<String, Object> firstCapture = captures.getFirst();

        return (String) firstCapture.get("id");
    }
}
