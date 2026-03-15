package dev.mkeo102.logger.loggingStrategy.impl;

import dev.mkeo102.logger.LoggerType;
import dev.mkeo102.logger.loggingStrategy.LoggingStrategy;

import java.util.List;

public class MultiLoggingStrategy implements LoggingStrategy {

    private final List<LoggingStrategy> providers;

    public MultiLoggingStrategy(List<LoggingStrategy> providers) {
        this.providers = providers;
    }

    @Override
    public void log(LoggerType type, String message) {
        providers.forEach(provider -> provider.log(type, message));
    }

    @Override
    public void silentLog(LoggerType type, String message) {
        providers.forEach(provider -> provider.silentLog(type, message));
    }
}
