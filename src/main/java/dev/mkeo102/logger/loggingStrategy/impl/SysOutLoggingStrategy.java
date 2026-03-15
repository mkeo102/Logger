package dev.mkeo102.logger.loggingStrategy.impl;

import dev.mkeo102.logger.LoggerType;
import dev.mkeo102.logger.loggingStrategy.LoggingStrategy;

import java.time.LocalDateTime;

import static dev.mkeo102.logger.TerminalColors.*;

public class SysOutLoggingStrategy implements LoggingStrategy {

    @Override
    public void log(LoggerType type, String message) {
        // Using String.format here for the time formatting
        String formatted = String.format("%s[%s] [%tT] %s%s", type.getTerminalColor(), type.getTypeInfo(), LocalDateTime.now(), message, RESET);
        System.out.println(formatted);
    }

    @Override
    public void silentLog(LoggerType type, String message) {
        String formatted = format("{color}{message}{color-reset}", type.getTerminalColor(), message, RESET);
        System.out.println(formatted);
    }
}
