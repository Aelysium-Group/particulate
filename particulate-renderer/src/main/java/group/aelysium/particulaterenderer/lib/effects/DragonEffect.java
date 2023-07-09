package group.aelysium.particulaterenderer.lib.effects;

import group.aelysium.particulaterenderer.ParticulateRenderer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;

public class DragonEffect extends Effect {

    public DragonEffect(int id) {
        super(id);
    }

    @Override
    public void play(Location location) {
        EnderDragon dragon = (EnderDragon) location.getWorld().spawnEntity(location, EntityType.ENDER_DRAGON);
        dragon.setSilent(true);
        dragon.setInvisible(true);
        this.killDragon(dragon);
    }

    @Override
    public void pause(Location location) {
    }

    private void killDragon(EnderDragon dragon) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ParticulateRenderer.getPlugin(ParticulateRenderer.class), () -> {
            dragon.setHealth(0);
        }, 2);
    }

    public static final class Builder {
        private Integer id;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public DragonEffect build() throws Throwable {
            if(this.id == null) throw new NullPointerException();
            return new DragonEffect(
                    this.id
            );
        }
    }
}
