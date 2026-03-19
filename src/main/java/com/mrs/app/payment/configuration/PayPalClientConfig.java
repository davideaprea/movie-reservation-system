package com.mrs.app.payment.configuration;

import com.mrs.app.payment.dto.PayPalClientConfigProps;
import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.authentication.ClientCredentialsAuthModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalClientConfig {
    @Bean
    public PaypalServerSdkClient configServerClient(PayPalClientConfigProps configProps) {
        return new PaypalServerSdkClient.Builder()
                .httpClientConfig(configBuilder -> configBuilder
                        .timeout(0))
                .clientCredentialsAuth(new ClientCredentialsAuthModel.Builder(
                        configProps.clientId(),
                        configProps.clientSecret()
                ).build())
                .environment(configProps.environment())
                .build();
    }
}
