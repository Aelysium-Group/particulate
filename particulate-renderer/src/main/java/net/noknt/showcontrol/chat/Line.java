package net.noknt.showcontrol.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class Line {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ComponentBuilder component;

        Builder() {}

        public Builder blank() {
            component = new ComponentBuilder("");
            return this;
        }
        public Builder blank(String text) {
            component = new ComponentBuilder(text);
            return this;
        }
        public Builder text(String text) {
            component.append(text).bold(false).underlined(false).obfuscated(false).italic(false).strikethrough(false);
            return this;
        }
        public Builder style(String style) {
            if(style.contains("underline")) component.underlined(true);
            if(style.contains("bold")) component.bold(true);
            if(style.contains("italic")) component.italic(true);
            if(style.contains("obfuscated")) component.obfuscated(true);
            if(style.contains("strikethrough")) component.strikethrough(true);
            return this;
        }
        public Builder color(ChatColor color) {
            component.color(color);
            return this;
        }

        public Builder click(String event, String value) {
            if(event.equals("run_command")) component.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,value));
            if(event.equals("suggest_command")) component.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,value));
            if(event.equals("open_url")) component.event(new ClickEvent(ClickEvent.Action.OPEN_URL,value));
            return this;
        }
        public Builder hover(String event, BaseComponent[] value) {
            if(event.equals("show_text")) component.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,value));
            if(event.equals("show_achievement")) component.event(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT,value));
            if(event.equals("show_entity")) component.event(new HoverEvent(HoverEvent.Action.SHOW_ENTITY,value));
            if(event.equals("show_item")) component.event(new HoverEvent(HoverEvent.Action.SHOW_ITEM,value));
            return this;
        }

        public ComponentBuilder build() {
            return component;
        }
    }
}
