package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

public class MoveMsg extends Message{

    private VCSApp app;
    private Entity src;
    private Vec2int coordinates;

    public MoveMsg(VCSApp app, Entity src, Vec2int coordinates) {
        super(MessageType.MOVE_ORDER);
        this.app = app;
        this.src = src;
        this.coordinates = coordinates;
    }
}
