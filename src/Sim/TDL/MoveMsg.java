package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

public class MoveMsg extends Message{

    private Vec2int pos;

    public MoveMsg(VCSApp app, Entity src, Entity receiver, Vec2int pos) {
        super(MessageType.MOVE_ORDER, app, src.getId(), receiver.getId(), (String.format("%s: %s",src, "J12.3")));
        this.pos = pos;
    }

    public Vec2int getPos(){
        return pos;
    }

    @Override
    public String getMsgDetail() {
        return String.format("Move Mission Assignment Message:\nFrom: %s\nTo: %s\nTarget Position: %s", getSrcID(), getTargetID(), pos.toString());
    }
}
