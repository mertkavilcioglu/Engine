package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import UI.UIColorManager;

import java.awt.*;
import java.util.ArrayList;

//TODO: Infoda bulunan bilgileri arraylist içinde ver ve popup içinde ayrı ayrı bas her ent için


public class KnownInfosMsg extends Message{

    private final ArrayList<Entity> knownEntities;
    private final Entity.Side side;

    public KnownInfosMsg(VCSApp app, Entity.Side side, String srcID, String targetID, ArrayList<Entity> knownEntities) {
        super(MessageType.KNOWN_INFO, app, srcID, targetID, srcID, "J2.0");
        this.knownEntities = knownEntities;
        this.side = side;
    }

    public KnownInfosMsg(KnownInfosMsg other){
        super(other);
        this.knownEntities = other.knownEntities;
        this.side = other.side;
    }

    @Override
    public String getMsgDetail() {
        String result = "";

        for(Entity e : knownEntities){
            String newInfo = String.format(
                    """
                            ::::::::::UNIT INFO::::::::::
                            Unit Name: %s
                            Unit ID: %s
                            Unit Type: %s
                            Unit Position: %s
                            Unit Speed: %s""",
                    e.getName(), e.getId(), e.getType().getName(), e.getPos().toString(), e.getSpeed().toString());
            result = String.format("%s%s", result, newInfo);
        }
        getApp().debugLog(result);
        return result;
    }

    @Override
    public Color getColor() {
        return UIColorManager.J2_COLOR;
    }

    public ArrayList<Entity> getKnownEntities() {
        return knownEntities;
    }

    public Entity.Side getSide() {
        return side;
    }

    @Override
    public Message copy() {
        return new KnownInfosMsg(this);
    }
}
