package net.noknt.showcontrol.controlPanel.chat;

import net.md_5.bungee.api.ChatColor;
import net.noknt.showcontrol.ShowControl;
import net.noknt.showcontrol.chat.Chat;
import net.noknt.showcontrol.chat.Message;
import net.noknt.showcontrol.chat.Line;
import net.noknt.showcontrol.chat.Mute;
import org.bukkit.entity.Player;

import java.util.Map;

public class FxPanel extends Chat {
    Line line = new Line();

    public FxPanel() {}

    public void build(Message message) {
        int linkID = 0;
        String slotID = "";
        Line.Builder lineBuilder = Line.builder().blank().text("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        lineBuilder.text("-----------<").color(ChatColor.DARK_GRAY).text(" CHAT FX CONTROLLER ").color(ChatColor.AQUA).text(">-----------").color(ChatColor.DARK_GRAY).text("\n");
        int lineBreak = 0;
        for(Map.Entry<String, Object> controlPanelLoopMap : ShowControl.getInstance().getMap("controlPanel").entrySet()) {
            if (controlPanelLoopMap.getKey().matches("control-panel\\.[0-9]*\\.name")) { // Only continue if the current emitter entry is a name entry
                linkID = (Integer)ShowControl.getInstance().getMap("controlPanel").get("control-panel."+controlPanelLoopMap.getKey().replaceFirst("control-panel\\.([0-9]*)\\.name","$1")+".link-id");
                slotID = controlPanelLoopMap.getKey().replaceFirst("control-panel\\.([0-9]*)\\.name","$1");
                if(ShowControl.getInstance().getLinkStatus().containsKey(linkID)) {
                    if(ShowControl.getInstance().getLinkStatus().get(linkID) == 1) lineBuilder.text((String)controlPanelLoopMap.getValue()).click("run_command", "/sc control fx toggle " + linkID + " " + slotID).color(ChatColor.WHITE);
                    else lineBuilder.text((String)controlPanelLoopMap.getValue()).click("run_command", "/sc control fx toggle " + linkID + " " + slotID).color(ChatColor.YELLOW);
                } else {
                    lineBuilder.text((String)controlPanelLoopMap.getValue()).click("run_command", "/sc control fx toggle " + linkID + " " + slotID).color(ChatColor.YELLOW);
                }
                lineBreak++;
                if(lineBreak > 4) {
                    lineBuilder.text("\n");
                    lineBreak = 0;
                }
            }
        }
        if(lineBreak != 0) lineBuilder.text("\n");

        message.add(lineBuilder.text("----------------").color(ChatColor.DARK_GRAY).text("[MUTE CHAT]").color(ChatColor.RED).style("underline").click("run_command","/sc chat mute").text("----------------").color(ChatColor.DARK_GRAY).build());
        for(Map.Entry<Integer, Integer> entry : ShowControl.getInstance().getLinkStatus().entrySet()) {
        }
    }
}
