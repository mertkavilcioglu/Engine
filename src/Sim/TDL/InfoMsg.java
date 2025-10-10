package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Sim.Radar;
import Vec.Vec2int;

public class InfoMsg extends Message{

    private String name;
    private Entity.Side side;
    private Vec2int pos;
    private Vec2int speed;
    private int radarRange;
    private Entity.Type type;

    public InfoMsg(VCSApp app, Entity src, Entity receiver) {
        super(MessageType.ENTITY_INFO, app, src, receiver, (src + ": Info msg"));
        this.name = src.getName();
        this.side = src.getSide();
        this.pos = src.getPos();
        this.speed = src.getSpeed();
        this.radarRange = ((Radar) src.getComponent("Radar")).getRange();
        this.type = src.getType();
    }

    @Override
    public String getMsgDetail() {
        return String.format("Info Message:\n Unit Name: %s\nUnit Type: %s\nUnit Position: %s\nUnit Radar Range: %d", name, type.getName(), pos.toString(), radarRange);
    }

    }
