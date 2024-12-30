package com.opensource.example.handler;

import com.opensource.service.register.handler.HandlerConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomHandlerConsumer implements HandlerConsumer<Long> {

    private static final Logger logger = LoggerFactory.getLogger(CustomHandlerConsumer.class);

    @Override
    public void accept(Long s) {
        logger.info("execute custom handler consumer...");
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("end to execute custom handler consumer.");
    }
}
