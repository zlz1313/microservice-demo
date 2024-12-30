package com.opensource.example.utils;

import jakarta.annotation.Nullable;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringEnvironmentUtils implements EnvironmentAware {

    private static Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        SpringEnvironmentUtils.environment = environment;
    }

    public static @Nullable String getProperty(String key) {
        if (environment != null) {
            return environment.getProperty(key);
        }
        return null;
    }

    public static String getProperty(String key, String defaultValue) {
        if (environment != null) {
            return environment.getProperty(key, defaultValue);
        }
        return defaultValue;
    }
}
