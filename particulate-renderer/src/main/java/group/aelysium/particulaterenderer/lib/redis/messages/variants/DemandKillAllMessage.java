package group.aelysium.particulaterenderer.lib.redis.messages.variants;

import com.google.gson.JsonObject;
import group.aelysium.particulaterenderer.lib.redis.messages.GenericMessage;
import group.aelysium.particulaterenderer.lib.redis.messages.MessageType;

public class DemandKillAllMessage extends GenericMessage {
    public DemandKillAllMessage() {
        super(MessageType.DEMAND_KILL_ALL);
    }
    public DemandKillAllMessage(String rawMessage, char[] authKey) {
        super(rawMessage, authKey, MessageType.DEMAND_KILL_ALL);
    }

    @Override
    public JsonObject toJSON() {
        return super.toJSON();
    }
}
