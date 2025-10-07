package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

import java.util.concurrent.RecursiveTask;

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

    public VCSApp getApp(){
        return app;
    }

    public Entity getSrc(){
        return src;
    }

    public Entity getTarget(){
        return target;
    }

    public int getTime(){
        return time;
    }
}
