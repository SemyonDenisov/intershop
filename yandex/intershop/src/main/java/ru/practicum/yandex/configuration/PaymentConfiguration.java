package ru.practicum.yandex.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.payment.client.api.DefaultApi;

@Configuration
public class PaymentConfiguration {
    @Value("${payment-service-name}")
    String paymentServiceName;
    @Value("${payment-service-port}")
    Integer paymentServicePort;

    @Bean
    public DefaultApi defaultApi() {
        return new DefaultApi(paymentServiceName, paymentServicePort);
    }
}
