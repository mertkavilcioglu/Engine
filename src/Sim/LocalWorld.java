package Sim;

import App.VCSApp;
import Sim.TDL.InfoMsg;
import Sim.TDL.Message;
import Vec.Vec2int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class LocalWorld {

    private ArrayList<Entity> entities = new ArrayList<>();
    private HashMap<String, Entity> entityHashMap;

    public LocalWorld() {
    }

    public Entity createEntity(String id, String eName, Entity.Side eSide, Vec2int pos, Vec2int speed, Entity.Type type){
        Entity ent = new Entity(eName, eSide,pos,speed, type);
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
    }

    public void readEntityInfo(Message msg){
        if(entityHashMap.containsKey(msg.getSrcID()))
            updateEntity(msg.getSrcID(),((InfoMsg) msg).getName(), ((InfoMsg) msg).getSide(), ((InfoMsg) msg).getPos(), ((InfoMsg) msg).getSpeed(), ((InfoMsg) msg).getType());
        else
            createEntity(msg.getSrcID(),((InfoMsg) msg).getName(), ((InfoMsg) msg).getSide(), ((InfoMsg) msg).getPos(), ((InfoMsg) msg).getSpeed(), ((InfoMsg) msg).getType());
    }

}
