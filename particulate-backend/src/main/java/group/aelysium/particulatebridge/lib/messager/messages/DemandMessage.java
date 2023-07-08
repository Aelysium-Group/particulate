package group.aelysium.particulatebridge.lib.messager.messages;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.lettuce.core.KeyValue;

import java.util.ArrayList;
import java.util.List;

public class DemandMessage extends GenericMessage {
    private String channelId;
    private int effectId;

    public String getChannelID() {
        return channelId;
    }
    public int getEffectID() {
        return effectId;
    }

    public DemandMessage(List<KeyValue<String, JsonPrimitive>> parameters) {
        super(MessageType.DEMAND_PING);

        if(!DemandMessage.validateParameters(ValidParameters.toList(), parameters))
            throw new IllegalStateException("Unable to construct Redis message! There are missing parameters!");

        parameters.forEach(entry -> {
            String key = entry.getKey();
            JsonPrimitive value = entry.getValue();

            switch (key) {
                case DemandToggleOnMessage.ValidParameters.CHANNEL_ID -> this.channelId = value.getAsString();
                case DemandToggleOnMessage.ValidParameters.EFFECT_ID -> this.effectId = value.getAsInt();
            }
        });
    }
    public DemandMessage(String rawMessage, char[] authKey, List<KeyValue<String, JsonPrimitive>> parameters) {
        super(rawMessage, authKey, MessageType.DEMAND_PING);

        if(!DemandMessage.validateParameters(ValidParameters.toList(), parameters))
            throw new IllegalStateException("Unable to construct Redis message! There are missing parameters!");

        parameters.forEach(entry -> {
            String key = entry.getKey();
            JsonPrimitive value = entry.getValue();

            switch (key) {
                case DemandToggleOnMessage.ValidParameters.CHANNEL_ID -> this.channelId = value.getAsString();
                case DemandToggleOnMessage.ValidParameters.EFFECT_ID -> this.effectId = value.getAsInt();
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
    public static DemandMessage from(String dataChannel, int effectId) {
        List<KeyValue<String, JsonPrimitive>> parameters = new ArrayList<>();
        parameters.add(KeyValue.just(ValidParameters.CHANNEL_ID, new JsonPrimitive(dataChannel)));
        parameters.add(KeyValue.just(ValidParameters.EFFECT_ID, new JsonPrimitive(effectId)));

        return new DemandMessage(parameters);
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
