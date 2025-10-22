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

    public InfoMsg(VCSApp app, String srcID, String targetID, String srcName, Entity.Side srcSide, Vec2int srcPos, Vec2int srcSpeed, Entity.Type srcType) {
        super(MessageType.ENTITY_INFO, app, srcID, targetID, (String.format("%s: %s",srcName, srcType.getPpliCode())));
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
                        "Unit Type: %s\n" +
                        "Unit Position: %s\n" +
                        "Unit Speed: %s",
                name, type.getName(), pos.toString(), speed.toString());
    }

    }
