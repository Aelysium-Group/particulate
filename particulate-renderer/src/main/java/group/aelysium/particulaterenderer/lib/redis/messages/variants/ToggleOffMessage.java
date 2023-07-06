package group.aelysium.particulaterenderer.lib.redis.messages.variants;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import group.aelysium.particulaterenderer.lib.redis.messages.GenericMessage;
import group.aelysium.particulaterenderer.lib.redis.messages.MessageType;
import io.lettuce.core.KeyValue;

import java.util.ArrayList;
import java.util.List;

public class ToggleOffMessage extends GenericMessage {
    private int channelId;
    private int effectId;

    public int getChannelID() {
        return channelId;
    }
    public int getEffectID() {
        return effectId;
    }

    public ToggleOffMessage(List<KeyValue<String, JsonPrimitive>> parameters) {
        super(MessageType.CONTROL_TOGGLE_OFF);

        if(!ToggleOffMessage.validateParameters(ValidParameters.toList(), parameters))
            throw new IllegalStateException("Unable to construct message! There are missing parameters!");

        parameters.forEach(entry -> {
            String key = entry.getKey();
            JsonPrimitive value = entry.getValue();

            switch (key) {
                case ValidParameters.CHANNEL_ID -> this.channelId = value.getAsInt();
                case ValidParameters.EFFECT_ID -> this.effectId = value.getAsInt();
            }
        });
    }
    public ToggleOffMessage(String rawMessage, char[] authKey, List<KeyValue<String, JsonPrimitive>> parameters) {
        super(rawMessage, authKey, MessageType.CONTROL_TOGGLE_OFF);

        if(!ToggleOffMessage.validateParameters(ValidParameters.toList(), parameters))
            throw new IllegalStateException("Unable to construct Redis message! There are missing parameters!");

        parameters.forEach(entry -> {
            String key = entry.getKey();
            JsonPrimitive value = entry.getValue();

            switch (key) {
                case ValidParameters.CHANNEL_ID -> this.channelId = value.getAsInt();
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

    /**
     * Create a new DemandMessage targeting the specific datachannel
     * @param dataChannel The datachannel to target.
     * @return A DemandMessage.
     */
    public static ToggleOffMessage from(int dataChannel) {
        List<KeyValue<String, JsonPrimitive>> parameters = new ArrayList<>();
        parameters.add(KeyValue.just(ValidParameters.CHANNEL_ID, new JsonPrimitive(dataChannel)));
        parameters.add(KeyValue.just(ValidParameters.EFFECT_ID, new JsonPrimitive(dataChannel)));

        return new ToggleOffMessage(parameters);
    }

    public interface ValidParameters {
        String CHANNEL_ID = "id";
        String EFFECT_ID = "eid";

        static List<String> toList() {
            List<String> list = new ArrayList<>();
            list.add(CHANNEL_ID);
            list.add(EFFECT_ID);

            return list;
        }
    }
}
