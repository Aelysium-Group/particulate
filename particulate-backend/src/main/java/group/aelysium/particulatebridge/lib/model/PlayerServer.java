package group.aelysium.particulatebridge.lib.model;

public interface PlayerServer extends Sortable {

    /**
     * Get the number of players on this server
     * @return The player count
     */
    int getPlayerCount();

    /**
     * Get the weight of this server
     * @return The weight of this server
     */
    int getWeight();

    /**
     * Get the soft player cap of this server
     * @return The soft player cap of this server
     */
    int getSoftPlayerCap();
    /**
     * Get the hard player cap of this server
     * @return The hard player cap
     */
    int getHardPlayerCap();
}
