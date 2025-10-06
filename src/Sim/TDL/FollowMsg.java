package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

public class FollowMsg extends Message{

    private VCSApp app;
    private Entity src, target;
    private int time;
    public FollowMsg(VCSApp app, Entity src, Entity target, int time) {
        super(MessageType.FOLLOW_ORDER);
        this.app = app;
        this.src = src;
        this.target = target;
        this.time = time;

    }
}
