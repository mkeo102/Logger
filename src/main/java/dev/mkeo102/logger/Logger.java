package dev.mkeo102.logger;

import java.time.LocalTime;

@SuppressWarnings("unused")
public class Logger implements TerminalColors {

    private final String name;

    private final boolean debug;

    private Logger(Class<?> clazz){
        this.name = clazz.getName();
        this.debug = false;
    }

    private Logger(String name){
        this.name = name;
        this.debug = false;
    }

    private Logger(Class<?> clazz, boolean debug){
        this.name = clazz.getName();
        this.debug = debug;
    }

    private Logger(String name, boolean debug){
        this.name = name;
        this.debug = debug;
    }

    public void log(LoggerType type, String message){
        LocalTime time = LocalTime.now();
        String formatted = String.format("%s[%s:%s:%s][%s] %s%s",type.getTerminalColor(),time.getHour(),time.getMinute(),time.getSecond(),type.getTypeInfo(),message,RESET);
        System.out.println(formatted);
    }

    public void silentlog(LoggerType type, String message){
        LocalTime time = LocalTime.now();
        String formatted = String.format("%s %s%s",type.getTerminalColor(),message,RESET);
        System.out.println(formatted);
    }

    public void silentlog(LoggerType type, String message, Object... formats){
        for(Object o : formats){
            message = message.replaceFirst("\\{}", o.toString());
        }
        silentlog(type,message);
    }

    public void log(LoggerType type, String message, Object... formats){
        for(Object o : formats){
            message = message.replaceFirst("\\{}", o.toString());
        }
        log(type,message);
    }

    public void info(String message){
        log(new InfoType(),message);
    }

    public void info(String message, Object... formats){
        for(Object o : formats){
            message = message.replaceFirst("\\{}", o.toString());
        }
        info(message);
    }


    public void warning(String message){
        log(new WarningType(),message);
    }

    public void warning(String message, Object... formats){
        for(Object o : formats){
            message = message.replaceFirst("\\{}", o.toString());
        }
        warning(message);
    }


    public void error(String message){
        log(new ErrorType(),message);
    }

    public void error(String message, Object... formats){
        for(Object o : formats){
            message = message.replaceFirst("\\{}", o.toString());
        }
        error(message);
    }

    public void exception(Throwable t){
        log(new ExceptionType(),t.getMessage());
        StackTraceElement[] stackTrace = t.getStackTrace();
        for (StackTraceElement ste : stackTrace){
            silentlog(new StackTraceType(),"\tat {}.{}({}:{})",ste.getClassName(),ste.getMethodName(), ste.getFileName(),ste.getLineNumber());
        }
    }

    public void debug(String message){
        if(debug)
            log(new DebugType(),message);
    }

    public void debug(String message, Object... formats){
        for(Object o : formats){
            message = message.replaceFirst("\\{}", o.toString());
        }
        debug(message);
    }


    public static Logger getLogger(Class<?> clazz){
        return new Logger(clazz);
    }

    public static Logger getLogger(String name){
        return new Logger(name);
    }

    public static Logger getLogger(Class<?> clazz, boolean debug){
        return new Logger(clazz,debug);
    }

    public static Logger getLogger(String name, boolean debug){
        return new Logger(name,debug);
    }

    private static class InfoType extends LoggerType {
        public InfoType() {
            super("INFO", RESET);
        }
    }
    private static class WarningType extends LoggerType {
        public WarningType() {
            super("WARNING", YELLOW);
        }
    }
    private static class ErrorType extends LoggerType {
        public ErrorType() {
            super("ERROR", RED);
        }
    }
    private static class DebugType extends LoggerType {
        public DebugType() {
            super("DEBUG", GREEN);
        }
    }

    private static class ExceptionType extends LoggerType {
        public ExceptionType() {
            super("EXCEPTION", RED);
        }
    }
    private static class StackTraceType extends LoggerType {
        public StackTraceType() {
            super("", RED);
        }
    }

}
