package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import UI.UIColorManager;

import java.awt.*;

public class FollowMsg extends Message{
    private Entity followTarget;
    private int time;

    public FollowMsg(VCSApp app, Entity src, Entity receiver, Entity followTarget, int time) {
        super(MessageType.FOLLOW_ORDER, app, src.getId(), receiver.getId(), src.getId(), "J12.2");
        this.time = time;
        this.followTarget = followTarget;
    }

    public int getTime(){
        return time;
    }

    public Entity getFollowTarget(){
        return followTarget;
    }

    @Override
    public String getMsgDetail() {
        return String.format("Follow Mission Assignment Message:\nFrom: %s\nTo: %s\nTarget: %s", getSrcID(), getTargetID(), followTarget.getName());
    }

    @Override
    public Color getColor() {
        return UIColorManager.J12_COLOR;
    }
}
