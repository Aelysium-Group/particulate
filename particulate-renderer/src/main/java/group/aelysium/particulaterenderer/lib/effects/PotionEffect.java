package group.aelysium.particulaterenderer.lib.effects;

import group.aelysium.particulaterenderer.lib.model.DeltaLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class PotionEffect extends Effect {
    private org.bukkit.potion.PotionEffect potionEffect;
    private boolean global;
    private DeltaLocation area;

    protected PotionEffect(int id, PotionEffectType type, int duration, int amplifier, DeltaLocation area) {
        super(id);
        this.potionEffect = new org.bukkit.potion.PotionEffect(type, duration, amplifier, false, false, false);
        this.global = false;
        this.area = area;
    }
    protected PotionEffect(int id, org.bukkit.potion.PotionEffectType type, int duration, int amplifier) {
        super(id);
        this.potionEffect = new org.bukkit.potion.PotionEffect(type, duration, amplifier);
        this.global = true;
        this.area = null;
    }

    @Override
    public void play(Location location) {
        if (this.global) {
            for (Player player : location.getWorld().getPlayers())
                player.addPotionEffect(this.potionEffect);

            return;
        }

        for (Entity entity : location.getNearbyEntities(this.area.dx(), this.area.dy(), this.area.dz())) {
            if (entity instanceof Player)
                try {
                    Objects.requireNonNull(Bukkit.getPlayer(entity.getUniqueId())).addPotionEffect(this.potionEffect);
                } catch (Exception ignore) {}
        }
    }

    @Override
    public void pause(Location location) {
        if (this.global) {
            for (Player player : location.getWorld().getPlayers())
                player.removePotionEffect(this.potionEffect.getType());

            return;
        }

        for (Entity entity : location.getNearbyEntities(this.area.dx(), this.area.dy(), this.area.dz())) {
            if (entity instanceof Player)
                try {
                    Objects.requireNonNull(Bukkit.getPlayer(entity.getUniqueId())).removePotionEffect(this.potionEffect.getType());
                } catch (Exception ignore) {}
        }
    }

    public static final class Builder {
        private Integer id;
        private PotionEffectType type;
        private Integer duration;
        private Integer amplifier;
        private Boolean global = false;
        private DeltaLocation area;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }
        public Builder setType(PotionEffectType type) {
            this.type = type;
            return this;
        }
        public Builder setDuration(Integer duration) {
            this.duration = duration;
            return this;
        }
        public Builder setAmplifier(Integer amplifier) {
            this.amplifier = amplifier;
            return this;
        }
        public Builder setGlobal(Boolean global) {
            this.global = global;
            return this;
        }
        public Builder setArea(DeltaLocation area) {
            this.area = area;
            return this;
        }

        public PotionEffect build() throws Throwable {
            if(this.id == null) throw new NullPointerException();
            if(this.duration == null) throw new NullPointerException();
            if(this.amplifier == null) throw new NullPointerException();

            if(this.global == false) {
                if(this.area == null) throw new NullPointerException();

                return new PotionEffect(
                        this.id,
                        this.type,
                        this.duration,
                        this.amplifier,
                        this.area
                );
            }

            return new PotionEffect(
                    this.id,
                    this.type,
                    this.duration,
                    this.amplifier
            );
        }
    }
}
