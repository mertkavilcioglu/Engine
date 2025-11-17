package Sim;

import App.VCSApp;
import Sim.TDL.InfoMsg;
import Sim.TDL.KnownInfosMsg;
import Sim.TDL.Message;
import Sim.TDL.SurveillanceMsg;
import Vec.Vec2int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class LocalWorld {

    private ArrayList<Entity> entities = new ArrayList<>();
    private HashMap<String, Entity> entityHashMap = new HashMap<>();
    private Entity src;

    public LocalWorld(Entity src) {
        this.src = src;
    }

    public Entity createEntity(String id, String eName, Entity.Side eSide, Vec2int pos, Vec2int speed, Entity.Type type){
        Entity ent = new Entity(src.w, eName, eSide,pos,speed, type);
        ent.maxSpeed = ent.getSpeed().getHypotenuseAsInt();
        if(ent.maxSpeed == 0)
            ent.maxSpeed = 4;
        ent.setId(id);
        entities.add(ent);
        entityHashMap.put(id, ent);
        ent.isLocal = true;
        return ent;
    }

    public void update(int deltaTime) {
        for (Entity entity : entities) {
            entity.update(deltaTime);
        }
    }

    public void updateEntity(String id, String eName, Entity.Side eSide, Vec2int pos, Vec2int speed, Entity.Type type){
        Entity ent = entityHashMap.get(id);
        ent.setName(eName);
        ent.setSide(eSide);
        ent.setPos(pos);
        ent.setSpeed(speed);
        ent.setType(type);
        entityHashMap.replace(id, ent);
    }

    public void readEntityInfo(Message msg){
        if(entityHashMap.containsKey(msg.getSrcID()))
            updateEntity(msg.getSrcID(),((InfoMsg) msg).getName(), ((InfoMsg) msg).getSide(), ((InfoMsg) msg).getPos(), ((InfoMsg) msg).getSpeed(), ((InfoMsg) msg).getType());
        else
            createEntity(msg.getSrcID(),((InfoMsg) msg).getName(), ((InfoMsg) msg).getSide(), ((InfoMsg) msg).getPos(), ((InfoMsg) msg).getSpeed(), ((InfoMsg) msg).getType());

        src.w.app.debugLog(String.format("Info Message of %s has taken by %s.\n", msg.getSrcID(), src.getId()));
    }

    public void readKnownInfo(Message msg){
        for(Entity ent : ((KnownInfosMsg) msg).getKnownEntities()){
            if(entityHashMap.containsKey(ent.getId()))
                updateEntity(ent.getId(), ent.getName(), ent.getSide(), ent.getPos(), ent.getSpeed(), ent.getType());
            else
                createEntity(ent.getId(), ent.getName(), ent.getSide(), ent.getPos(), ent.getSpeed(), ent.getType());
        }
    }


    public void readSurveillanceInfo(Message msg){
        SurveillanceMsg sMsg = (SurveillanceMsg) msg;
        if (entityHashMap.containsKey(sMsg.getSeenID())){
          updateEntity(sMsg.getSeenID(), sMsg.getSeenName(), sMsg.getSeenSide(), sMsg.getSeenPos(), sMsg.getSeenSpeed(), sMsg.getSeenType());
        } else createEntity(sMsg.getSeenID(), sMsg.getSeenName(), sMsg.getSeenSide(), sMsg.getSeenPos(), sMsg.getSeenSpeed(), sMsg.getSeenType());

        src.w.app.debugLog(String.format("Surveillance Information of %s from %s taken by %s.\n", sMsg.getSeenID(), sMsg.getSrcID(), sMsg.getTargetID()));
    }

    public HashMap<String, Entity> getEntityHashMap() {
        return entityHashMap;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
