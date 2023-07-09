package group.aelysium.particulatebridge.lib.messager.messages;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.lettuce.core.KeyValue;

import java.util.ArrayList;
import java.util.List;

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
