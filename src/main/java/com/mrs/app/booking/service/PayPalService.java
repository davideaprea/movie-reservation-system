package com.mrs.app.booking.service;

import com.mrs.app.booking.dto.internal.PayPalOrderDto;
import com.mrs.app.booking.dto.internal.PayPalCapturedOrder;
import com.mrs.app.booking.dto.internal.PayPalOrder;
import com.mrs.app.booking.dto.internal.PayPalTokenDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Base64;

@Service
public class PayPalService {
    private final String encodedCredentials;

    private final RestClient restClient;

    private final Object threadLock = new Object();

    private volatile String accessToken;

    private volatile Instant tokenExpiryTime;

    public PayPalService(
            @Value("${paypal.base-url}")
            String baseUrl,

            @Value("${paypal.client-id}")
            String clientId,

            @Value("${paypal.secret}")
            String clientSecret,

            RestClient.Builder restClientBuilder
    ) {
        this.encodedCredentials = encodeCredentials(clientId, clientSecret);

        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .requestInterceptor(configInterceptor())
                .build();
    }

    private String encodeCredentials(String clientId, String clientSecret) {
        String plainCredentials = clientId + ":" + clientSecret;
        return Base64.getEncoder().encodeToString(plainCredentials.getBytes());
    }

    private ClientHttpRequestInterceptor configInterceptor() {
        return (request, body, execution) -> {
            if (!request.getURI().getPath().contains("/v1/oauth2/token")) {
                refreshAccessToken();

                request.getHeaders().setBearerAuth(accessToken);
            }

            return execution.execute(request, body);
        };
    }

    private void refreshAccessToken() {
        if (accessToken != null && isTokenValid()) return;

        synchronized (threadLock) {
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
        MultiValueMap<String, String> reqBody = new LinkedMultiValueMap<>();

        reqBody.add("grant_type", "client_credentials");

        return restClient.post()
                .uri("/v1/oauth2/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(reqBody)
                .retrieve()
                .body(PayPalTokenDetails.class);
    }

    public PayPalOrder createOrder(PayPalOrderDto dto) {
        return restClient.post()
                .uri("/v2/checkout/orders")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public PayPalCapturedOrder captureOrder(String orderId) {
        return restClient.post()
                .uri("/v2/checkout/orders/" + orderId + "/capture")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public void refundPayment(String captureId) {
        restClient.post()
                .uri("/v2/payments/captures/" + captureId + "/refund")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
