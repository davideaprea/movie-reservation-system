package com.mrs.app.payment.configuration;

import com.mrs.app.payment.dto.gateway.StripeClientConfigProps;
import com.stripe.net.RequestOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeRequestOptionsBuilderConfig {
    @Bean
    public RequestOptions.RequestOptionsBuilder fromConfigProps(StripeClientConfigProps configProps) {
        return RequestOptions.builder().setApiKey(configProps.apiKey());
    }
}
