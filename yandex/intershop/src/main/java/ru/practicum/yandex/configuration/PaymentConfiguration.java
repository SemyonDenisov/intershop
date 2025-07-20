package ru.practicum.yandex.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.payment.client.api.DefaultApi;

@Configuration
public class PaymentConfiguration {
    @Bean
    public DefaultApi defaultApi() {
        return new DefaultApi();
    }
}
