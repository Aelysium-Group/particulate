package group.aelysium.particulaterenderer.command;

import cloud.commandframework.Command;
import cloud.commandframework.paper.PaperCommandManager;
import group.aelysium.particulaterenderer.ParticulateRenderer;
import group.aelysium.particulaterenderer.central.API;
import group.aelysium.particulaterenderer.central.PluginLogger;
import group.aelysium.particulaterenderer.lib.redis.RedisService;
import group.aelysium.particulaterenderer.lib.redis.messages.variants.DemandKillAllMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

public final class CommandParticulate {
    public static void create(PaperCommandManager<CommandSender> manager) {
        manager.command(kill(manager));
        manager.command(reload(manager));
    }

    private static Command.Builder<CommandSender> kill(PaperCommandManager<CommandSender> manager) {
        API api = ParticulateRenderer.getAPI();
        final Command.Builder<CommandSender> builder = api.getCommandManager().commandBuilder("particulate");

        return builder.literal("kill")
                .permission("particulate.kill")
                .handler(context -> manager.taskRecipe().begin(context)
                        .asynchronous(commandContext -> {
                            try {
                                context.getSender().sendMessage(Component.text("Killing all effects...", NamedTextColor.GRAY));
                                DemandKillAllMessage message = new DemandKillAllMessage();

                                api.getService(RedisService.class).publish(message);
                            } catch (Exception e) {
                                context.getSender().sendMessage(Component.text("An error stopped us from processing your request! " + e.getMessage(), NamedTextColor.RED));
                            }
                        }).execute());
    }

    private static Command.Builder<CommandSender> reload(PaperCommandManager<CommandSender> manager) {
        API api = ParticulateRenderer.getAPI();
        PluginLogger logger = api.getLogger();
        final Command.Builder<CommandSender> builder = api.getCommandManager().commandBuilder("particulate");

        return builder.literal("reload")
                .permission("particulate.reload")
                .handler(context -> manager.taskRecipe().begin(context)
                        .asynchronous(commandContext -> {
                            try {
                                context.getSender().sendMessage(Component.text("Reloading ParticulateRenderer...", NamedTextColor.GRAY));

                                ParticulateRenderer.getLifecycle().stop();
                                if(!ParticulateRenderer.getLifecycle().start()) context.getSender().sendMessage(Component.text("Unable to complete reload!", NamedTextColor.RED));
                                context.getSender().sendMessage(Component.text("Reload complete!", NamedTextColor.GREEN));
                            } catch (Exception e) {
                                context.getSender().sendMessage(Component.text("An error stopped us from processing the request! " + e.getMessage(), NamedTextColor.RED));
                            }
                        }).execute());
    }
}