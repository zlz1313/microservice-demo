package com.opensource.service.register.listener;

import com.netflix.appinfo.InstanceInfo;
import com.opensource.service.register.handler.HandlerConsumer;
import com.opensource.service.register.handler.RegisterDelayConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MicroServiceGracefulRegisterListener implements ApplicationListener<ApplicationEvent>, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(MicroServiceGracefulRegisterListener.class);

    @Value("${grace.rule.startDelayTime:60}")
    private long startDelayTime;

    @Value("${eureka.instance.leaseRenewalIntervalInSeconds:30}")
    private long leaseRenewalIntervalInSeconds;

    private final static AtomicBoolean isReady = new AtomicBoolean(false);
    private final static AtomicBoolean isDelayed = new AtomicBoolean(false);

    private final ServiceRegistry serviceRegistry;
    private Registration registration;

    private List<HandlerConsumer<?>> handlerConsumers;

    public MicroServiceGracefulRegisterListener(ServiceRegistry serviceRegistry, Registration registration) {
        this.serviceRegistry = serviceRegistry;
        this.registration = registration;
        loadHandlers();
    }

    private void loadHandlers() {
        LinkedList<HandlerConsumer<?>> consumers = new LinkedList<>();
        for (HandlerConsumer<?> handler : ServiceLoader.load(HandlerConsumer.class)) {
            consumers.add(handler);
            logger.info("found {} handler consumer.", handler.getClass().getSimpleName());
        }
        if (consumers.isEmpty()) {
            logger.info("not found any handler consumer, using default delay handler.");
            this.handlerConsumers = Arrays.asList(new RegisterDelayConsumer());
        } else {
            this.handlerConsumers = Collections.unmodifiableList(consumers);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            onApplicationEvent(event);
        }
    }

    private void onApplicationEvent(ApplicationReadyEvent event) {
        if (registration != null && isReady.compareAndSet(false, true)) {
            long startTs = System.currentTimeMillis();
            for (HandlerConsumer handlerConsumer : handlerConsumers) {
                HandlerConsumer.accept(handlerConsumer, startDelayTime);
            }
            if (System.currentTimeMillis() - startTs < leaseRenewalIntervalInSeconds) {
                logger.warn("discovery status send interval too small, then force sleep.");
                new RegisterDelayConsumer().accept(leaseRenewalIntervalInSeconds - (System.currentTimeMillis() - startTs));
            }
            serviceRegistry.setStatus(registration, InstanceInfo.InstanceStatus.UP.name());
            logger.info("microservice end to graceful register.");
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}
