package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

public class MoveMsg extends Message{

    private Vec2int pos;

    public MoveMsg(VCSApp app, Entity src, Entity receiver, Vec2int pos) {
        super(MessageType.MOVE_ORDER, app, src, receiver);
        this.pos = pos;
    }

    public Vec2int getPos(){
        return pos;
    }
}
