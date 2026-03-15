package dev.mkeo102.logger;

import dev.mkeo102.logger.loggingStrategy.LoggingStrategy;
import dev.mkeo102.logger.loggingStrategy.impl.SysOutLoggingStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory to standardise creation and management of all loggers in existence
 */
public class LoggerFactory {

    private LoggingStrategy fallbackProvider = new SysOutLoggingStrategy();
    private final Map<LoggerType, LoggingStrategy> providers = new HashMap<>();

    private final Map<String, Logger> createdLoggers = new HashMap<>();

    private boolean muted = false;
    private boolean debug = false;
    public LoggerFactory() {

    }

    /**
     * @return A default logger with default name
     */
    public Logger getLogger() {
        return getLogger("default-logger");
    }

    /**
     * @param clazz the class to assign this logger to
     * @return a logger with the name of the provided class
     */
    public Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }


    /**
     * Returns a logger with the assigned name
     * @param name the name to assign to this logger to
     * @return a logger with the provided name or a cached logger if existing
     */
    public Logger getLogger(String name) {

        if(createdLoggers.containsKey(name)) {
            return createdLoggers.get(name);
        }

        Logger logger = new Logger(name, fallbackProvider, new HashMap<>(providers));
        logger.setMuted(muted);
        logger.setDebug(debug);
        createdLoggers.put(name, logger);
        return logger;
    }

    /**
     * Sets the fallback for all loggers created in future from this factory
     * @implNote DOES NOT UPDATE CACHED LOGGERS
     * @param fallbackProvider the fallback provider to use
     */
    public void setFallbackProvider(LoggingStrategy fallbackProvider) {
        this.fallbackProvider = fallbackProvider;
    }

    /**
     * Mutes/unmutes all loggers cached by this factory
     * @param muted whether to mute or unmute the logger
     */
    public void setLoggersMuted(boolean muted) {
        this.muted = muted;
        this.createdLoggers.forEach((name, logger) -> logger.setMuted(muted));
    }

    /**
     * Sets the provider to use for the specified type for all future created loggers
     * @implNote DOES NOT UPDATE CACHED PROVIDERS
     * @param type the type to associate the provider for
     * @param provider the provider to use
     * @return _
     */
    public LoggingStrategy setProvider(LoggerType type, LoggingStrategy provider) {
        return providers.put(type, provider);
    }

    /**
     * Removes the provider associated with the provided type
     * @implNote DOES NOT UPDATE CACHED PROVIDERS
     * @param type the logging level
     * @return null if no previous provider present, else the removed provider
     */
    public LoggingStrategy removeProvider(LoggerType type) {
        return this.providers.remove(type);
    }

    /**
     * Resets all associated providers and sets fallbackProvider to a {@link SysOutLoggingStrategy}
     */
    public void resetProviders() {
        this.providers.clear();
        this.fallbackProvider = new SysOutLoggingStrategy();
    }

    /**
     * Clears the cache of created loggers (could be useful with loggers that should be garbage collected)
     */
    public void clearCache() {
        this.createdLoggers.clear();
    }

    /**
     * Enabled/disables debug output in all loggers cached by this factory
     * @param debug whether to enable or disable the debug
     */
    public void setDebugEnabled(boolean debug) {
        this.debug = debug;
        this.createdLoggers.forEach((name, logger) -> logger.setDebug(debug));
    }

}
