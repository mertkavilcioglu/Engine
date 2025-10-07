package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

public class MoveMsg extends Message{

    private VCSApp app;
    private Entity src;
    private Vec2int pos;

    public MoveMsg(VCSApp app, Entity src, Vec2int pos) {
        super(MessageType.MOVE_ORDER);
        this.app = app;
        this.src = src;
        this.pos = pos;
    }

    public VCSApp getApp(){
        return app;
    }

    public Entity getSrc(){
        return src;
    }

    public Vec2int getPos(){
        return pos;
    }
}
