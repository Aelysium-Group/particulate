package group.aelysium.particulaterenderer.lib.redis.messages.variants;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import group.aelysium.particulaterenderer.lib.redis.messages.GenericMessage;
import group.aelysium.particulaterenderer.lib.redis.messages.MessageType;
import io.lettuce.core.KeyValue;

import java.util.ArrayList;
import java.util.List;

public class DemandToggleOffMessage extends GenericMessage {
    private String channelId;
    private int effectId;

    public String getChannelID() {
        return channelId;
    }
    public int getEffectID() {
        return effectId;
    }

    public DemandToggleOffMessage(List<KeyValue<String, JsonPrimitive>> parameters) {
        super(MessageType.DEMAND_TOGGLE_OFF);

        if(!DemandToggleOffMessage.validateParameters(ValidParameters.toList(), parameters))
            throw new IllegalStateException("Unable to construct message! There are missing parameters!");

        parameters.forEach(entry -> {
            String key = entry.getKey();
            JsonPrimitive value = entry.getValue();

            switch (key) {
                case ValidParameters.CHANNEL_ID -> this.channelId = value.getAsString();
                case ValidParameters.EFFECT_ID -> this.effectId = value.getAsInt();
            }
        });
    }
    public DemandToggleOffMessage(String rawMessage, char[] authKey, List<KeyValue<String, JsonPrimitive>> parameters) {
        super(rawMessage, authKey, MessageType.DEMAND_TOGGLE_OFF);

        if(!DemandToggleOffMessage.validateParameters(ValidParameters.toList(), parameters))
            throw new IllegalStateException("Unable to construct Redis message! There are missing parameters!");

        parameters.forEach(entry -> {
            String key = entry.getKey();
            JsonPrimitive value = entry.getValue();

            switch (key) {
                case ValidParameters.CHANNEL_ID -> this.channelId = value.getAsString();
                case ValidParameters.EFFECT_ID -> this.effectId = value.getAsInt();
            }
        });
    }

    @Override
    public JsonObject toJSON() {
        JsonObject object = super.toJSON();
        JsonObject parameters = new JsonObject();

        parameters.add(ValidParameters.CHANNEL_ID, new JsonPrimitive(this.channelId));
        parameters.add(ValidParameters.EFFECT_ID, new JsonPrimitive(this.effectId));

        object.add(MasterValidParameters.PARAMETERS, parameters);

        return object;
    }

    public interface ValidParameters {
        String CHANNEL_ID = "cid";
        String EFFECT_ID = "eid";

        static List<String> toList() {
            List<String> list = new ArrayList<>();
            list.add(CHANNEL_ID);
            list.add(EFFECT_ID);

            return list;
        }
    }
}
