package group.aelysium.particulatebridge.lib.messager.messages;

import java.util.ArrayList;
import java.util.List;

public class MessageType {
    public static Mapping RESPONSE_FAILURE = new Mapping(1, "RESPONSE_FAILURE");
    public static Mapping RESPONSE_SUCCESS = new Mapping(2, "RESPONSE_SUCCESS");

    public static Mapping LOGIN = new Mapping(100, "LOGIN");

    public static Mapping CONTROL_DEMAND = new Mapping(200, "CONTROL_DEMAND");
    public static Mapping CONTROL_TOGGLE_ON = new Mapping(201, "CONTROL_TOGGLE_ON");
    public static Mapping CONTROL_TOGGLE_OFF = new Mapping(202, "CONTROL_TOGGLE_OFF");

    public static List<Mapping> toList() {
        List<Mapping> list = new ArrayList<>();
        list.add(RESPONSE_FAILURE);
        list.add(RESPONSE_SUCCESS);
        list.add(LOGIN);
        list.add(CONTROL_DEMAND);
        list.add(CONTROL_TOGGLE_ON);
        list.add(CONTROL_TOGGLE_OFF);

        return list;
    }

    public static Mapping getMapping(String name) {
        return toList().stream().filter(entry -> entry.name() == name).findFirst().orElseThrow(NullPointerException::new);
    }
    public static Mapping getMapping(int id) {
        return toList().stream().filter(entry -> entry.id() == id).findFirst().orElseThrow(NullPointerException::new);
    }

    public record Mapping (Integer id, String name) {
        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
