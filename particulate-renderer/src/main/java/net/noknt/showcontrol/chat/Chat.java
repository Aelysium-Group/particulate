package net.noknt.showcontrol.chat;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import java.util.Map;

public class Chat {
    private ComponentBuilder component;

    public Chat() {}

    public void send(Player player,Message message) {
        for(Map.Entry<Integer,ComponentBuilder> messageLines : message.getLines().entrySet()) {
            player.spigot().sendMessage(messageLines.getValue().create());
        }
    }

    public void mute() {
    }
}
