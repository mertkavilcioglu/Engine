package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

import java.awt.*;
import java.util.ArrayList;

//TODO: Infoda bulunan bilgileri arraylist içinde ver ve popup içinde ayrı ayrı bas her ent için


public class KnownInfosMsg extends Message{

    private  ArrayList<Entity> knownEntities;
    private Entity.Side side;

    public KnownInfosMsg(VCSApp app, Entity.Side side, String srcID, String targetID, String srcName, ArrayList<Entity> knownEntities) {
        super(MessageType.KNOWN_INFO, app, srcID, targetID, srcID, "J2.0");
        this.knownEntities = knownEntities;
        this.side = side;
    }

    @Override
    public String getMsgDetail() {
        String result = "";

        for(Entity e : knownEntities){
            String newInfo = String.format(
                    "Precise Participant Location and Identification (PPLI):\n" +
                            "Unit Name: %s\n" +
                            "Unit ID: %s\n" +
                            "Unit Type: %s\n" +
                            "Unit Position: %s\n" +
                            "Unit Speed: %s\n\n",
                    e.getName(), e.getType().getName(),getSrcID(), e.getPos().toString(), e.getSpeed().toString());
            result = String.format("%s%s", result, newInfo);
        }
        getApp().debugLog(result);
        return result;
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    public ArrayList<Entity> getKnownEntities() {
        return knownEntities;
    }

    public Entity.Side getSide() {
        return side;
    }
}
