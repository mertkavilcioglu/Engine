package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

import java.util.ArrayList;

//TODO: Infoda bulunan bilgileri arraylist içinde ver ve popup içinde ayrı ayrı bas her ent için


public class KnownInfosMsg extends Message{

    private  ArrayList<Entity> knownEntities;

    public KnownInfosMsg(VCSApp app, String srcID, String targetID, String srcName, Entity.Type srcType, ArrayList<Entity> knownEntities) {
        super(MessageType.KNOWN_INFO, app, srcID, targetID,  (String.format("%s: %s",srcName, srcType.getPpliCode())));
        this.knownEntities = knownEntities;
    }

    @Override
    public String getMsgDetail() {
        String result = "";

        for(Entity e : getApp().world.entityHashMap.get(getSrcID()).getLocalWorld().getEntities()){
            String newInfo = String.format(
                    "Precise Participant Location and Identification (PPLI):\n" +
                            "Unit Name: %s\n" +
                            "Unit ID: %s\n" +
                            "Unit Type: %s\n" +
                            "Unit Position: %s\n" +
                            "Unit Speed: %s\n\n",
                    e.getName(), e.getType().getName(),getSrcID(), e.getPos().toString(), e.getSpeed().toString());
            result.concat(newInfo);
        }
        getApp().debugLog(result);
        return result;
    }

    public ArrayList<Entity> getKnownEntities() {
        return knownEntities;
    }
}
