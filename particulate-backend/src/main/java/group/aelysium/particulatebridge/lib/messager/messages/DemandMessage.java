package group.aelysium.particulatebridge.lib.websocket.messages.variants;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import group.aelysium.particulatebridge.lib.websocket.messages.WebsocketMessageType;
import io.lettuce.core.KeyValue;

import java.util.ArrayList;
import java.util.List;

public class ResponseFailureWebsocketMessage extends ResponseWebsocketMessage {

    public ResponseFailureWebsocketMessage(List<KeyValue<String, JsonPrimitive>> parameters) {
        super(WebsocketMessageType.RESPONSE_FAILURE);

        if(!ResponseFailureWebsocketMessage.validateParameters(ValidParameters.toList(), parameters))
            throw new IllegalStateException("Unable to construct Redis message! There are missing parameters!");

        parameters.forEach(entry -> {
            String key = entry.getKey();
            JsonPrimitive value = entry.getValue();

            switch (key) {
                case ValidParameters.RESPONSE_MESSAGE -> this.responseMessage = value.getAsString();
            }
        });
    }
    public ResponseFailureWebsocketMessage(String rawMessage, char[] authKey, List<KeyValue<String, JsonPrimitive>> parameters) {
        super(WebsocketMessageType.RESPONSE_FAILURE, rawMessage, authKey);

        if(!ResponseFailureWebsocketMessage.validateParameters(ValidParameters.toList(), parameters))
            throw new IllegalStateException("Unable to construct Redis message! There are missing parameters!");

        parameters.forEach(entry -> {
            String key = entry.getKey();
            JsonPrimitive value = entry.getValue();

            switch (key) {
                case ValidParameters.RESPONSE_MESSAGE -> this.responseMessage = value.getAsString();
            }
        });
    }

    @Override
    public JsonObject toJSON() {
        JsonObject object = super.toJSON();
        JsonObject parameters = new JsonObject();

        parameters.add(ValidParameters.RESPONSE_MESSAGE, new JsonPrimitive(this.responseMessage));

        object.add(MasterValidParameters.PARAMETERS, parameters);

        return object;
    }

    public static ResponseFailureWebsocketMessage from(String responseMessage) {
        List<KeyValue<String, JsonPrimitive>> parameters = new ArrayList<>();
        parameters.add(KeyValue.just(ValidParameters.RESPONSE_MESSAGE, new JsonPrimitive(responseMessage)));

        return new ResponseFailureWebsocketMessage(parameters);
    }

    public interface ValidParameters {
        String RESPONSE_MESSAGE = "m";

        static List<String> toList() {
            List<String> list = new ArrayList<>();
            list.add(RESPONSE_MESSAGE);

            return list;
        }
    }
}
