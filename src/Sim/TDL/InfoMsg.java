package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Sim.Radar;
import Vec.Vec2int;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InfoMsg extends Message{

    private String name;
    private Entity.Side side;
    private Vec2int pos;
    private Vec2int speed;
    private int radarRange;
    private Entity.Type type;

    public InfoMsg(VCSApp app, Entity src, List<Entity> receivers) {
        super(MessageType.ENTITY_INFO, app, src.getId(), receivers, (String.format("%s: %s",src, src.getPpliCode())));
        this.name = src.getName();
        this.side = src.getSide();
        this.pos = src.getPos();
        this.speed = src.getSpeed();
        //this.radarRange = ((Radar) src.getComponent("Radar")).getRange();
        this.type = src.getType();
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
