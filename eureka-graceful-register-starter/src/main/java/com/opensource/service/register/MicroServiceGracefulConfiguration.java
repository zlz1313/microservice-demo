package com.opensource.service.register;

import com.opensource.service.register.listener.MicroServiceGracefulRegisterListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "grace.rule.enabled", matchIfMissing = true)
public class MicroServiceGracefulConfiguration {

    @Autowired(required = false)
    private Registration registration;

    @Autowired(required = false)
    private ServiceRegistry serviceRegistry;

    @Bean
    public MicroServiceGracefulRegisterListener microServiceGracefulRegisterListener() {
        return new MicroServiceGracefulRegisterListener(serviceRegistry, registration);
    }
}
