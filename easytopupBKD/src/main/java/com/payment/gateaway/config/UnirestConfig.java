package com.payment.gateaway.config;
import jakarta.annotation.PostConstruct;
import kong.unirest.Unirest;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UnirestConfig {

    @PostConstruct
    public void init() {
        Unirest.config()
                .concurrency(200, 20)
                .connectTimeout(5000)
                .socketTimeout(10000)
                .automaticRetries(true);
    }
}
