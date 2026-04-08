package io.github.davideaprea.configuration;

import com.stripe.net.RequestOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeRequestOptionsBuilderConfig {
    @Bean
    public RequestOptions.RequestOptionsBuilder fromConfigProps(@Value("${stripe.api-key}") String stripeApiKey) {
        return RequestOptions.builder().setApiKey(stripeApiKey);
    }
}
