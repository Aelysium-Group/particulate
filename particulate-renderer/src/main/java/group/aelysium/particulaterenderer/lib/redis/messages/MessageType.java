package group.aelysium.particulaterenderer.lib.redis.messages;

import java.util.ArrayList;
import java.util.List;

public class MessageType {
    public static Mapping CONTROL_DEMAND = new Mapping(200, "CONTROL_DEMAND");
    public static Mapping CONTROL_TOGGLE_ON = new Mapping(201, "CONTROL_TOGGLE_ON");
    public static Mapping CONTROL_TOGGLE_OFF = new Mapping(202, "CONTROL_TOGGLE_OFF");

    public static List<Mapping> toList() {
        List<Mapping> list = new ArrayList<>();
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
