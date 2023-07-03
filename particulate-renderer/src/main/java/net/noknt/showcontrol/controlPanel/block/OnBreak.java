package net.noknt.showcontrol.controlPanel.block;

import net.noknt.showcontrol.ShowControl;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

public class OnBreak implements Listener {

    @EventHandler
    public void onBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();
        String blockLocation = block.getWorld().getName()+" "+block.getX()+" "+block.getY()+" "+block.getZ();
        String slotID;

        if(ShowControl.getInstance().getMap("controlPanel").containsValue(blockLocation)) { // Check if the placed block is important

            for (Map.Entry<String, Object> entry : ShowControl.getInstance().getMap("controlPanel").entrySet()) {
                if (entry.getValue().equals(blockLocation)) {
                    slotID = entry.getKey().replaceAll("control-panel\\.([0-9]*)\\.loc","$1");
                    // Get the key of the array object containing the location of the block and then get the link-id from it
                    // Remove the current link-id from linkStatus (Since the link is now turned off)
                    ShowControl.getInstance().getLinkStatus().put(Integer.valueOf(slotID), -1);
                }
            }
        }


    }
}
