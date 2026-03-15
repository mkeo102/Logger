package dev.mkeo102.logger.loggingStrategy.impl;

import dev.mkeo102.logger.LoggerType;
import dev.mkeo102.logger.loggingStrategy.LoggingStrategy;

/**
 * A logging strategy that doesn't output anything <br/>
 * Intended to disable types of output
 */
public class NoOpLoggingStrategy implements LoggingStrategy {
    @Override
    public void log(LoggerType type, String message) {

    }

    @Override
    public void silentLog(LoggerType type, String message) {

    }
}
