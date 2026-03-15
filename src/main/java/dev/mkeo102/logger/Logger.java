package dev.mkeo102.logger;

import dev.mkeo102.logger.loggingStrategy.LoggingStrategy;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Logger implements TerminalColors {

    private boolean muted = false;
    private boolean debug = true;

    private final String name;

    private final LoggingStrategy fallbackProvider;
    private final Map<LoggerType, LoggingStrategy> providers;


    Logger(Class<?> clazz, LoggingStrategy fallbackProvider, Map<LoggerType, LoggingStrategy> providers) {
        this.name = clazz.getName();
        this.fallbackProvider = fallbackProvider;
        this.providers = providers;
    }

    Logger(String name, LoggingStrategy fallbackProvider, Map<LoggerType, LoggingStrategy> providers) {
        this.name = name;
        this.fallbackProvider = fallbackProvider;
        this.providers = providers;
    }

    public void log(LoggerType type, String message) {
        if(muted()) return;
        // Using String.format here for the simple time formatting
        String formatted = String.format("%s[%s] [%tT] %s%s", type.getTerminalColor(), type.getTypeInfo(), LocalDateTime.now(), message, RESET);

        LoggingStrategy provider = providers.getOrDefault(type, fallbackProvider);
        provider.log(type, formatted);

    }

    public void silentLog(LoggerType type, String message) {
        if(muted()) return;
        String formatted = format("{color}{message}{color-reset}", type.getTerminalColor(), message, RESET);

        LoggingStrategy provider = providers.getOrDefault(type, fallbackProvider);
        provider.silentLog(type, formatted);
    }

    public void silentLog(LoggerType type, String message, Object... formats) {
        silentLog(type, format(message, formats));
    }

    public void log(LoggerType type, String message, Object... formats) {
        log(type, format(message, formats));
    }

    public void info() {
        info("");
    }

    public void info(String message) {
        log(new InfoType(), message);
    }

    public void info(String message, Object... formats) {
        info(format(message, formats));
    }

    public void warning() {
        warning("");
    }

    public void warning(String message) {
        log(new WarningType(), message);
    }

    public void warning(String message, Object... formats) {

        warning(format(message, formats));
    }

    public void error(String message) {
        log(new ErrorType(), message);
    }

    public void error() {
        error("");
    }

    public void error(String message, Object... formats) {
        error(format(message, formats));
    }

    public void exception(Throwable t) {
        log(new ExceptionType(), "{type} {message}", t.getClass().getName(), t.getMessage());
        StackTraceElement[] stackTrace = t.getStackTrace();
        for (StackTraceElement ste : stackTrace) {
            log(new StackTraceType(), "    at {class}.{method}({file name}:{line})", ste.getClassName(), ste.getMethodName(), ste.getFileName(), ste.getLineNumber());
        }
    }

    public void debug(String message) {
        if (debug)
            log(new DebugType(), message);
    }

    public void debug() {
        debug("");
    }

    public void debug(String message, Object... formats) {
        debug(format(message, formats));
    }




    static String format(String format, Object... args) {
        if(args == null) args = new Object[]{null};

        Pattern replacePattern = Pattern.compile("(?<!\\\\)\\{[^}]*}");


        for(Object o : args) {
            Matcher match = replacePattern.matcher(format);
            String safeArg = o == null ? "null" : Matcher.quoteReplacement(escape(o.toString()));

            if(!match.find()) throw new IllegalArgumentException(format("Too many arguments provided for format string: {}", escape(format)));
            format = match.replaceFirst(safeArg);
        }

        if(replacePattern.matcher(format).find()) throw new MissingFormatArgumentException(format);

        format = format.replace("\\{", "{");

        return format;
    }

    static String escape(String input ){
        return input.replace("{", "\\{");
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean muted() {
        return muted;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean debugEnabled() {
        return debug;
    }


    public static class InfoType extends LoggerType {
        public static final InfoType instance = new InfoType();
        public InfoType() {
            super("INFO", RESET);
        }
    }

    public static class WarningType extends LoggerType {
        public static final WarningType instance = new WarningType();
        public WarningType() {
            super("WARNING", YELLOW);
        }
    }

    public static class ErrorType extends LoggerType {
        public static final ErrorType instance = new ErrorType();

        public ErrorType() {
            super("ERROR", RED);
        }
    }

    public static class DebugType extends LoggerType {
        public static final DebugType instance = new DebugType();
        public DebugType() {
            super("DEBUG", GREEN);
        }
    }

    public static class ExceptionType extends LoggerType {
        public static final ExceptionType instance = new ExceptionType();
        public ExceptionType() {
            super("EXCEPTION", RED);
        }
    }

    public static class StackTraceType extends LoggerType {
        public static final StackTraceType instance = new StackTraceType();

        public StackTraceType() {
            super("  TRACE  ", RED);
        }
    }


}
