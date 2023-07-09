package group.aelysium.particulaterenderer.lib.effects;

import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Collection;

public class CrystalEffect extends Effect {
    private Location beamTarget;

    public CrystalEffect(int id, Location beamTarget) {
        super(id);
        this.beamTarget = beamTarget;
    }

    @Override
    public void play(Location location) {
        Collection<Entity> entities = location.getNearbyEntities(0.5,0.5,0.5);
        for(Entity entity : entities)
            if(entity instanceof EnderCrystal)
                return;


        EnderCrystal crystal = (EnderCrystal) location.getWorld().spawnEntity(location, EntityType.ENDER_CRYSTAL);

        crystal.setInvulnerable(true);
        crystal.setGravity(false);
        crystal.setBeamTarget(this.beamTarget);
        crystal.setShowingBottom(false);
    }

    @Override
    public void pause(Location location) {
        Collection<Entity> entities = location.getNearbyEntities(0.5,0.5,0.5);
        for(Entity entity : entities)
            if(entity instanceof EnderCrystal)
                entity.remove();
    }

    public static final class Builder {
        private Integer id;
        private Location beamTarget;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }
        public Builder setBeamTarget(Location beamTarget) {
            this.beamTarget = beamTarget;
            return this;
        }

        public CrystalEffect build() throws Throwable {
            if(this.id == null) throw new NullPointerException();
            if(this.beamTarget == null) throw new NullPointerException();
            return new CrystalEffect(
                    this.id,
                    this.beamTarget
            );
        }
    }
}
