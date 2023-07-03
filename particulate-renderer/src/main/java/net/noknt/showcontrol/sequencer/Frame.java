package net.noknt.showcontrol.sequencer;

import net.noknt.showcontrol.renderEngine.Effect;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class Frame {
    private String effectId;
    private Map<String, Object> arguments;
    private int offset;
    private static Frame instance;

    public static Frame getInstance() {
        return instance;
    }

    public Frame(String effectId, Map<String,Object> arguments, int offset) {
        instance = this;
        this.effectId = effectId;
        this.arguments = arguments;
        this.offset = offset;
    }

    public Map<String,Object> getArguments() {
        return this.arguments;
    }

    public void execute(Frame frame) {

    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String effectId;
        private Map<String, Object> arguments = new HashMap<>();
        private Throwable InvalidPropertiesFormatException;
        private Throwable NullPointerException;
        private static Frame instance;

        Builder() {}


    }
}
