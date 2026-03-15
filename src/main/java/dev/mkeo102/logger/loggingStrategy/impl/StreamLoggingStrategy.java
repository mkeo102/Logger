package dev.mkeo102.logger.loggingStrategy.impl;

import dev.mkeo102.logger.LoggerType;
import dev.mkeo102.logger.loggingStrategy.LoggingStrategy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static dev.mkeo102.logger.TerminalColors.RESET;

public class StreamLoggingStrategy implements LoggingStrategy {

    private final OutputStream out;

    public StreamLoggingStrategy(OutputStream out) {
        this.out = out;
    }


    @Override
    public void log(LoggerType type, String message) {
        // Using String.format here for the time formatting
        String formatted = String.format("%s[%s] [%tT] %s%s\n", type.getTerminalColor(), type.getTypeInfo(), LocalDateTime.now(), message, RESET);
        try {
            out.write(formatted.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignored){}
    }

    @Override
    public void silentLog(LoggerType type, String message) {
        String formatted = format("{color}{message}{color-reset}\n", type.getTerminalColor(), message, RESET);
        try {
            out.write(formatted.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignored){}
    }
}
