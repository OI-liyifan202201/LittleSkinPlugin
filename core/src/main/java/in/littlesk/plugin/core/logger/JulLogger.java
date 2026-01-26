package in.littlesk.plugin.core.logger;

import java.util.logging.Logger;

public class JulLogger implements PlatformLogger {
    private final java.util.logging.Logger logger;

    public JulLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warning(String message) {
        logger.warning(message);
    }

    @Override
    public void error(String message) {
        logger.severe(message);
    }

    @Override
    public void debug(String message) {
        logger.fine(message);
    }
}
