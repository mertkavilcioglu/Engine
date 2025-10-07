package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

public class AttackMsg extends Message {

    private VCSApp app;
    private Entity src, target;

    public AttackMsg(VCSApp app, Entity src, Entity target) {
        super(MessageType.ATTACK_ORDER);
        this.app = app;
        this.src = src;
        this.target = target;

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
}
