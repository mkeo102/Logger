package dev.mkeo102.logger.loggingStrategy;

import dev.mkeo102.logger.LoggerType;

import java.util.MissingFormatArgumentException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface LoggingStrategy {

    void log(LoggerType type, String message);

    void silentLog(LoggerType type, String message);

    default String format(String format, Object... args) {
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

    default String escape(String input ){
        return input.replace("{", "\\{");
    }

}
