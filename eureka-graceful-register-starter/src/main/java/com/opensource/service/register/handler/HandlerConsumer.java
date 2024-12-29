package com.opensource.service.register.handler;

import java.util.function.Consumer;

public interface HandlerConsumer<T> extends Consumer<T> {

    static <T> void accept(HandlerConsumer<T> consumer, T target) {
        if (consumer != null) {
            consumer.accept(target);
        }
    }
}
