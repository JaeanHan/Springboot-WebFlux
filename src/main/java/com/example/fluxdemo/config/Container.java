package com.example.fluxdemo.config;

import com.example.fluxdemo.domain.Customer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class Container {
    @Bean
    public Sinks.Many<Customer> sinks() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
