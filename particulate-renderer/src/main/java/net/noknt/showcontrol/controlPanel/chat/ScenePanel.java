package net.noknt.showcontrol.controlPanel.chat;

import net.md_5.bungee.api.ChatColor;
import net.noknt.showcontrol.ShowControl;
import net.noknt.showcontrol.chat.Chat;
import net.noknt.showcontrol.chat.Message;
import net.noknt.showcontrol.chat.Line;

import java.util.Map;

public class ScenePanel extends Chat {
    Line line = new Line();

    public ScenePanel() {}

    public void build(Message message,String screenName) {
        String slotID;
        Line.Builder lineBuilder = Line.builder().blank().text("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        lineBuilder.text("---------<").color(ChatColor.DARK_GRAY).text(" CHAT SCENE CONTROLLER ").color(ChatColor.GOLD).text(">---------").color(ChatColor.DARK_GRAY).text("\n");
        int lineBreak = 0;
        lineBuilder.text("Currently Controlling: ").color(ChatColor.GRAY).text(screenName).color(ChatColor.AQUA).text("\n");
        for (Map.Entry<String, Object> scenesLoopMap : ShowControl.getInstance().getMap("scenes").entrySet()) {
            if (scenesLoopMap.getKey().matches("scenes\\.[A-z]*")) { // Only continue if the current scene entry is a name entry
                slotID = scenesLoopMap.getKey().replaceFirst("scenes\\.([A-z]*)", "$1");

                lineBuilder.text("["+slotID+"] ").click("run_command", "/sc control screen change "+screenName+" "+slotID).color(ChatColor.WHITE);

                lineBreak++;
                if(lineBreak > 4) {
                    lineBuilder.text("\n");
                    lineBreak = 0;
                }
            }
        }
        lineBuilder.text("\n");
        message.add(lineBuilder.text("----------------").color(ChatColor.DARK_GRAY).text("[MUTE CHAT]").color(ChatColor.RED).style("underline").click("run_command","/sc chat mute").text("----------------").color(ChatColor.DARK_GRAY).build());


    }
}
