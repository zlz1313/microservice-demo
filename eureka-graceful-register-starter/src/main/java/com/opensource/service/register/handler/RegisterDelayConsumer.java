package com.opensource.service.register.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterDelayConsumer implements HandlerConsumer<Long> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterDelayConsumer.class);

    @Override
    public void accept(Long sleepTime) {
        if (sleepTime == null) {
            return;
        }
        logger.info("try to delay {}s register...", sleepTime);
        try {
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException e) {
            logger.error("delay register failed.");
        }
    }
}
