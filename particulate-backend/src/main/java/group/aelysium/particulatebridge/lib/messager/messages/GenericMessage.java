package group.aelysium.particulatebridge.lib.messager.messages;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.lettuce.core.KeyValue;

import java.util.ArrayList;
import java.util.List;

public class GenericMessage {
    private final boolean sendable;
    private String rawMessage;

    private char[] authKey;
    private final MessageType.Mapping type;

    public boolean isSendable() { return this.sendable; }
    public String getRawMessage() { return this.rawMessage; }
    public char[] getAuthKey() { return this.authKey; }
    public MessageType.Mapping getType() { return this.type; }

    /**
     * Builds a sendable
     */
    protected GenericMessage(MessageType.Mapping type) {
        this.sendable = true;
        this.rawMessage = null;
        this.authKey = null;
        this.type = type;
    }

    /**
     * Builds a received
     */
    protected GenericMessage(String rawMessage, char[] authKey, MessageType.Mapping type) {
        this.sendable = false;
        this.rawMessage = rawMessage;
        this.authKey = authKey;
        this.type = type;
    }

    /**
     * Sign a sendable message with a private key.
     * @param privateKey The private key to sign with.
     * @throws IllegalStateException If you attempt to sign a received message. Or if the message is already signed.
     */
    public void signMessage(char[] privateKey) {
        if(!this.isSendable()) throw new IllegalStateException("Attempted to sign a received message! You can only sign sendable messages!");
        if(this.authKey != null) throw new IllegalStateException("Attempted to sign a message that was already signed!");
        this.authKey = privateKey;
    }

    /**
     * Returns the message as a string.
     * The returned string is actually the raw message that was received or is able to be sent through Redis.
     * @return The message as a string.
     */
    @Override
    public String toString() {
        if(this.rawMessage == null) this.rawMessage = this.toJSON().toString();
        return this.rawMessage;
    }

    public JsonObject toJSON() {
        JsonObject object = new JsonObject();

        object.add(MasterValidParameters.AUTH_KEY, new JsonPrimitive(String.valueOf(this.authKey)));
        object.add(MasterValidParameters.TYPE, new JsonPrimitive(String.valueOf(this.type)));

        return object;
    }

    /**
     * Checks if the two parameter lists (checking keys) match.
     * @param requiredParameters The parameters that are required.
     * @param parametersToCheck The parameter list to check.
     * @return `true` if all keys are present. `false` otherwise.
     */
    public static boolean validateParameters(List<String> requiredParameters, List<KeyValue<String, JsonPrimitive>> parametersToCheck) {
        List<String> keysToCheck = new ArrayList<>();
        parametersToCheck.forEach(entry -> keysToCheck.add(entry.getKey()));
        List<String> matches = requiredParameters.stream().filter(keysToCheck::contains).toList();
        return requiredParameters.size() == matches.size();
    }

    public static class Builder {
        private String rawMessage;
        private char[] authKey;
        private MessageType.Mapping type;
        private final List<KeyValue<String, JsonPrimitive>> parameters = new ArrayList<>();

        public Builder() {}

        public Builder setRawMessage(String rawMessage) {
            this.rawMessage = rawMessage;
            return this;
        }

        /**
         * Sets the private key for this message.
         * If you're building this message as a sendable message.
         * You shouldn't have to set this because RedisPublisher will sign the message,
         * when you attempt to publish it.
         * @param authKey The private key to set.
         * @return Builder
         */
        public Builder setAuthKey(char[] authKey) {
            this.authKey = authKey;
            return this;
        }
        public Builder setType(MessageType.Mapping type) {
            this.type = type;
            return this;
        }

        public Builder setParameter(String key, String value) {
            this.parameters.add(KeyValue.just(key, new JsonPrimitive(value)));
            return this;
        }
        public Builder setParameter(String key, JsonPrimitive value) {
            this.parameters.add(KeyValue.just(key, value));
            return this;
        }


        /**
         * Build a message which was received.
         * This should be a message which was previously built as a sendable message, and then was sent.
         * <p>
         * ## Required Parameters:
         * - `protocolVersion`
         * - `rawMessage`
         * - `privateKey`
         * - `type`
         * - `address`
         * - `origin`
         * @return A RedisMessage that can be published.
         * @throws IllegalStateException If the required parameters are not provided.
         */
        public GenericMessage buildReceived() {
            if (this.rawMessage == null)
                throw new IllegalStateException("You must provide `rawMessage` when building a receivable RedisMessage!");
            if (this.authKey == null)
                throw new IllegalStateException("You must provide `authKey` when building a receivable RedisMessage!");
            if (this.type == null)
                throw new IllegalStateException("You must provide `type` when building a receivable RedisMessage!");

            if(this.type == MessageType.DEMAND_PING)       return new DemandMessage(this.rawMessage, this.authKey, this.parameters);
            if(this.type == MessageType.DEMAND_TOGGLE_ON)  return new DemandToggleOnMessage(this.rawMessage, this.authKey, this.parameters);
            if(this.type == MessageType.DEMAND_TOGGLE_OFF) return new DemandToggleOffMessage(this.rawMessage, this.authKey, this.parameters);
            if(this.type == MessageType.DEMAND_KILL_ALL)   return new DemandKillAllMessage(this.rawMessage, this.authKey);

            return null;
        }

        /**
         * Build a message which can be sent via the RedisPublisher.
         * <p>
         * ## Required Parameters:
         * - `type`
         * <p>
         * ## Not Allowed Parameters:
         * - `protocolVersion`
         * @return A message that can be published.
         * @throws IllegalStateException If the required parameters are not provided. Or if protocolVersion is attempted to be set.
         */
        public GenericMessage buildSendable() {
            if(this.type == null) throw new IllegalStateException("You must provide `type` when building a sendable RedisMessage!");

            if(this.type == MessageType.DEMAND_PING)       return new DemandMessage(this.parameters);
            if(this.type == MessageType.DEMAND_TOGGLE_ON)  return new DemandToggleOnMessage(this.parameters);
            if(this.type == MessageType.DEMAND_TOGGLE_OFF) return new DemandToggleOffMessage(this.parameters);
            if(this.type == MessageType.DEMAND_KILL_ALL)   return new DemandKillAllMessage();

            return null;
        }
    }

    public static class Serializer {
        /**
         * Parses a raw string into a received message.
         * @param rawMessage The raw message to parse.
         * @return A received RedisMessage.
         */
        public GenericMessage parseReceived(String rawMessage) {
            System.out.println(rawMessage);
            Gson gson = new Gson();
            JsonObject messageObject = gson.fromJson(rawMessage, JsonObject.class);

            Builder redisMessageBuilder = new Builder();
            redisMessageBuilder.setRawMessage(rawMessage);

            messageObject.entrySet().forEach(entry -> {
                String key = entry.getKey();
                JsonElement value = entry.getValue();

                switch (key) {
                    case MasterValidParameters.AUTH_KEY -> redisMessageBuilder.setAuthKey(value.getAsString().toCharArray());
                    case MasterValidParameters.TYPE -> redisMessageBuilder.setType(MessageType.getMapping(value.getAsInt()));
                    case MasterValidParameters.PARAMETERS -> parseParams(value.getAsJsonObject(), redisMessageBuilder);
                }
            });

            return redisMessageBuilder.buildReceived();
        }

        private void parseParams(JsonObject object, Builder redisMessageBuilder) {
            object.entrySet().forEach(entry -> {
                String key = entry.getKey();
                JsonPrimitive value = entry.getValue().getAsJsonPrimitive();

                redisMessageBuilder.setParameter(key, value);
            });
        }

    }

    public interface MasterValidParameters {
        String AUTH_KEY = "k";
        String TYPE = "t";
        String PARAMETERS = "p";

        static List<String> toList() {
            List<String> list = new ArrayList<>();
            list.add(AUTH_KEY);
            list.add(TYPE);
            list.add(PARAMETERS);

            return list;
        }
    }
}

