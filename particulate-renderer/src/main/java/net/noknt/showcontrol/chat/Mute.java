package net.noknt.showcontrol.chat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.noknt.showcontrol.ShowControl;

import java.util.HashSet;
import java.util.Set;

public class Mute {
    private Set<String> mute = new HashSet<>();

    public Mute() {}

    public void muteChat() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(ShowControl.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (mute.contains(String.valueOf(event.getPlayer().getName()))) {
                            PacketContainer packet = event.getPacket();
                            String message = String.valueOf(packet.getChatComponents().read(0));
                            if (!message.equals("null")) {
                                event.setCancelled(true);
                            }

                        }
                    }
                });
    }

    public void add(String player) {
        mute.add(player);
    }
    public void remove(String player) {
        mute.remove(player);
    }
    public Set<String> getMute() {
        return mute;
    }
}
