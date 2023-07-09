package group.aelysium.particulatebridge.lib.messager.messages;

import java.util.ArrayList;
import java.util.List;

public class MessageType {
    public static Mapping DEMAND_PING = new Mapping(200, "CONTROL_DEMAND");
    public static Mapping DEMAND_TOGGLE_ON = new Mapping(201, "CONTROL_TOGGLE_ON");
    public static Mapping DEMAND_TOGGLE_OFF = new Mapping(202, "CONTROL_TOGGLE_OFF");
    public static Mapping DEMAND_KILL_ALL = new Mapping(203, "CONTROL_KILL_ALL");

    public static List<Mapping> toList() {
        List<Mapping> list = new ArrayList<>();
        list.add(DEMAND_PING);
        list.add(DEMAND_TOGGLE_ON);
        list.add(DEMAND_TOGGLE_OFF);
        list.add(DEMAND_KILL_ALL);

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
