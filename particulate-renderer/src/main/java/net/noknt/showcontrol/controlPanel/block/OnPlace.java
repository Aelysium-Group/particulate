package net.noknt.showcontrol.controlPanel.block;

import net.noknt.showcontrol.ShowControl;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;

public class OnPlace implements Listener {

    @EventHandler
    public void onPlaceEvent(BlockPlaceEvent event) {
        Block block = event.getBlock();
        String blockLocation = block.getWorld().getName()+" "+block.getX()+" "+block.getY()+" "+block.getZ();
        String slotID;
        if(ShowControl.getInstance().getMap("controlPanel").containsValue(blockLocation)) { // Check if the placed block is important
            for (Map.Entry<String, Object> entry : ShowControl.getInstance().getMap("controlPanel").entrySet()) {
                if (entry.getValue().equals(blockLocation)) {
                    // Get the key of the array object containing the location of the block and then get the link-id from it
                    slotID = entry.getKey().replaceAll("control-panel\\.([0-9]*)\\.loc","$1");

                    if(ShowControl.getInstance().getMap("controlPanel").containsKey("control-panel."+slotID+".single-use")) {
                        ShowControl.getInstance().getLinkStatus().put(Integer.valueOf(slotID), -1);
                        block.setType(Material.AIR);
                    } else {
                        ShowControl.getInstance().getLinkStatus().put(Integer.valueOf(slotID), 1);
                    }
                }
            }
        }


    }
}
