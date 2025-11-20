package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import UI.UIColorManager;
import Vec.Vec2int;

import java.awt.*;

public class InfoMsg extends Message{

    private String name;
    private Entity.Side side;
    private Vec2int pos;
    private Vec2int speed;
    private int radarRange;
    private Entity.Type type;

    public InfoMsg(VCSApp app, String srcID, String targetID, String srcName, Entity.Side srcSide, Vec2int srcPos, Vec2int srcSpeed, Entity.Type srcType) {
        super(MessageType.ENTITY_INFO, app, srcID, targetID, srcID, srcType.getPpliCode());
        this.name = srcName;
        this.side = srcSide;
        this.pos = srcPos;
        this.speed = srcSpeed;
        //this.radarRange = ((Radar) src.getComponent("Radar")).getRange();
        this.type = srcType;
    }

    @Override
    public String getMsgDetail() {
        return String.format(
                "Precise Participant Location and Identification (PPLI):\n" +
                        "Unit Name: %s\n" +
                        "Unit ID: %s\n" +
                        "Unit Type: %s\n" +
                        "Unit Position: %s\n" +
                        "Unit Speed: %s",
                name, getSrcID(), type.getName(), pos.toString(), speed.toString());
    }

    public String getName(){
        return name;
    }

    public Entity.Side getSide(){
        return side;
    }

    public Vec2int getPos(){
        return pos;
    }

    public Vec2int getSpeed(){
        return speed;
    }

    public Entity.Type getType(){
        return type;
    }

    @Override
    public Color getColor() {
        return UIColorManager.J2_COLOR;
    }
}
