package dev.mkeo102.logger;

/**
 * An object representing a type of logging. This is almost never worth using normally but it just made me implementing the normal logger easier.
 */
public class LoggerType implements TerminalColors {

    private final String typeInfo;
    private final String terminalColor;

    public LoggerType(String typeInfo, String color){
        this.typeInfo = typeInfo;
        this.terminalColor = color;
    }

    public String getTypeInfo(){
        return typeInfo;
    }

    public String getTerminalColor(){
        return terminalColor;
    }

}
