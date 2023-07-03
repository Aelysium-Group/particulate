package net.noknt.showcontrol.chat;

import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class Message {
    private Map<Integer,ComponentBuilder> lines = new LinkedHashMap<Integer,ComponentBuilder>();
    public Message() {}


    public Map<Integer,ComponentBuilder> getLines() {
        return lines;
    }

    /**
     * Adds a line to the message
     * @return void
     */
    public void add(ComponentBuilder line) {
        lines.put(lines.size(),line);
    }

    /**
     * Removes all lines from the message
     * @return void
     */
    public void clear() {
        lines.clear();
    }

    public void close() {
        Message message = null;
    }
}
