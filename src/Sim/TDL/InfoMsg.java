package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

public class InfoMsg extends Message{

    private String name;
    private Entity.Side side;
    private Vec2int pos;
    private Vec2int speed;
    private int radarRange;
    private Entity.Type type;

    public InfoMsg(VCSApp app, Entity src, Entity receiver, String name, Entity.Side side, Vec2int pos,
                   Vec2int speed, int radarRange, Entity.Type type) {
        super(MessageType.ENTITY_INFO, app, src, receiver);
        this.name = name;
        this.side = side;
        this.pos = pos;
        this.speed = speed;
        this.radarRange = radarRange;
        this.type = type;
    }
}
