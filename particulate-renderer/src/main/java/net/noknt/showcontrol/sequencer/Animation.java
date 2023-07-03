package net.noknt.showcontrol.sequencer;

import net.noknt.showcontrol.renderEngine.Effect;

import java.util.Map;

public class Animation {
    private Map<Integer ,Frame> frames;
    private Integer currentFrame;
    private Animation instance;

    public Animation(Map<Integer ,Frame> frames, boolean looping) {
        instance = this;
        this.frames = frames;
        this.currentFrame = 0;
    }

    public Integer size() { return this.frames.size(); }
    public Frame getCurrentFrame() { return this.frames.get(this.currentFrame); }
    public Integer getCurrentNumber() { return this.currentFrame; }
    public void step() {/*
        currentFrame++;
        if(currentFrame < this.size()) {
            Frame currentFrame = this.getCurrentFrame();
            Effect.getInstance().run(Effect.builder().unwrapFrame(currentFrame).build());
        } else {

        }*/
    }

}
