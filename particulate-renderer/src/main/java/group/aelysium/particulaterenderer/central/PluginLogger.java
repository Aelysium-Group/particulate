package group.aelysium.particulaterenderer.central;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

public class PluginLogger {
    private final Logger logger;

    public PluginLogger(Logger logger) {
        this.logger = logger;
    }

    public void log(String message) {
        logger.info(message);
    }

    public void log(String message, Throwable e) {
        logger.info(message, e);
    }

    public void debug(String message) {
        logger.info(message);
    }

    public void debug(String message, Throwable e) {
        logger.debug(message, e);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void warn(String message, Throwable e) {
        logger.warn(message, e);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable e) {
        logger.error(message, e);
    }

    public void send(Component message) {
        ParticulateRenderer.getAPI().getServer().getConsoleSender().sendMessage(message);
    }
}
