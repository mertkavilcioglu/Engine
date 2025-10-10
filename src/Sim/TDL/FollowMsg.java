package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

public class FollowMsg extends Message{
    private Entity followTarget;
    private int time;

    public FollowMsg(VCSApp app, Entity src, Entity receiver, Entity followTarget, int time) {
        super(MessageType.FOLLOW_ORDER, app, src, receiver, (src + ": Follow msg"));
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
        return String.format("Follow Message:\nFrom: %s\nTo: %s\nTarget: %s", getSrc().getName(), getReceiverEntity().getName(), followTarget.getName());
    }
}
