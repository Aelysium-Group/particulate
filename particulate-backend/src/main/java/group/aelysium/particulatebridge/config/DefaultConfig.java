package group.aelysium.particulatebridge.config;

import java.io.File;

public class DefaultConfig extends YAML {
    private static DefaultConfig config;
    private boolean debug = false;

    private char[] redis_privateKey;
    private String redis_host = "";
    private int redis_port = 3306;
    private String redis_user = "default";
    private String redis_password = "password";
    private String redis_dataChannel = "rustyConnector-sync";


    private char[] websocket_privateKey;
    private int websocket_port = 3306;

    private DefaultConfig(File configPointer, String template) {
        super(configPointer, template);
    }

    /**
     * Get the current config.
     * @return The config.
     */
    public static DefaultConfig getConfig() {
        return config;
    }

    /**
     * Create a new config for the proxy, this will delete the old config.
     * @return The newly created config.
     */
    public static DefaultConfig newConfig(File configPointer, String template) {
        config = new DefaultConfig(configPointer, template);
        return DefaultConfig.getConfig();
    }

    /**
     * Delete all configs associated with this class.
     */
    public static void empty() {
        config = null;
    }

    public char[] getRedis_privateKey() {
        return this.redis_privateKey;
    }

    public String getRedis_host() {
        return this.redis_host;
    }

    public int getRedis_port() {
        return this.redis_port;
    }

    public String getRedis_password() {
        return this.redis_password;
    }

    public String getRedis_user() {
        return this.redis_user;
    }

    public String getRedis_dataChannel() {
        return this.redis_dataChannel;
    }

    public char[] getWebsocket_privateKey() {
        return this.websocket_privateKey;
    }
    public int getWebsocket_port() {
        return this.websocket_port;
    }

    @SuppressWarnings("unchecked")
    public void register() throws IllegalStateException {
        try {
            this.debug = this.getNode(this.data,"debug",Boolean.class);
        } catch (Exception e) {
            this.debug = false;
        }

        // Redis
        this.redis_privateKey = this.getNode(this.data, "redis.private-key", String.class).toCharArray();
        if(this.redis_privateKey.length == 0) throw new IllegalStateException("Please configure your Redis settings.");

        this.redis_host = this.getNode(this.data, "redis.host", String.class);
        if(this.redis_host.equals("")) throw new IllegalStateException("Please configure your Redis settings.");

        this.redis_port = this.getNode(this.data, "redis.port", Integer.class);
        this.redis_user = this.getNode(this.data, "redis.user", String.class);
        this.redis_password = this.getNode(this.data, "redis.password", String.class);

        if(this.redis_password.length() != 0 && this.redis_password.length() < 16)
            throw new IllegalStateException("Your Redis password is to short! For security purposes, please use a longer password! "+this.redis_password.length()+" < 16");

        this.redis_dataChannel = this.getNode(this.data, "redis.data-channel", String.class);
        if(this.redis_dataChannel.equals(""))
            throw new IllegalStateException("You must pass a proper name for the data-channel to use with Redis!");


        // Websocket
        this.websocket_privateKey = this.getNode(this.data, "websocket.private-key", String.class).toCharArray();
        if(this.websocket_privateKey.length == 0) throw new IllegalStateException("Please configure your Websocket settings.");

        this.websocket_port = this.getNode(this.data, "websocket.port", Integer.class);
    }
}
