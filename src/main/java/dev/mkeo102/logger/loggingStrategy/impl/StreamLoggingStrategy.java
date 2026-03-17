package dev.mkeo102.logger.loggingStrategy.impl;

import dev.mkeo102.logger.LoggerType;
import dev.mkeo102.logger.loggingStrategy.LoggingStrategy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static dev.mkeo102.logger.TerminalColors.RESET;

/**
 * A logging strategy to output logs to an {@link OutputStream}
 */
public class StreamLoggingStrategy implements LoggingStrategy {

    private final OutputStream out;
    private final boolean colours;

    public StreamLoggingStrategy(OutputStream out) {
        this.out = out;
        this.colours = false;
    }

    public StreamLoggingStrategy(OutputStream out, boolean colours) {
        this.out = out;
        this.colours = colours;
    }


    @Override
    public void log(LoggerType type, String message) {
        // Using String.format here for the time formatting
        String formatted = String.format("%s[%s] [%tT] %s%s\n",
                colours ? type.getTerminalColor() : "",
                type.getTypeInfo(),
                LocalDateTime.now(),
                message,
                colours ? RESET : ""
        );
        try {
            out.write(formatted.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignored){}
    }

    @Override
    public void silentLog(LoggerType type, String message) {
        String formatted = format("{color}{message}{color-reset}\n",
                colours ? type.getTerminalColor() : "",
                message,
                colours ? RESET : ""
        );
        try {
            out.write(formatted.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ignored){}
    }
}
