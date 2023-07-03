package group.aelysium.particulaterenderer.central;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.lib.controlPanel.block.OnBreak;
import group.aelysium.particulaterenderer.lib.controlPanel.block.OnPlace;
import org.bukkit.plugin.PluginManager;

public class Lifecycle {
    protected boolean isRunning = false;

    public boolean isRunning() {
        return this.isRunning;
    }

    public boolean start() {

        loadConfigs();

        this.isRunning = true;
        return true;
    }
    public void stop() {}

    protected boolean loadConfigs() {
        return true;
    }

    protected boolean loadEvents() {
        try {
            PluginManager eventManager = ParticulateRenderer.getAPI().getServer().getPluginManager();

            eventManager.registerEvents(new OnPlace(), ParticulateRenderer.getAPI().accessPlugin());
            eventManager.registerEvents(new OnBreak(), ParticulateRenderer.getAPI().accessPlugin());

            return true;
        } catch (Exception ignore) {}
        return false;
    }
}
