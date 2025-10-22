package Sim;

import App.VCSApp;
import Vec.Vec2int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class LocalWorld {

    public final VCSApp app;
    private ArrayList<Entity> entities = new ArrayList<>();
    private HashMap<String, Entity> entityHashMap;

    public LocalWorld(VCSApp app) {
        this.app = app;
    }

    public Entity createEntity(String id, String eName, Entity.Side eSide, Vec2int pos, Vec2int speed, Entity.Type type){
        Entity ent = new Entity(eName, eSide,pos,speed, type);
        ent.maxSpeed = ent.getSpeed().getHypotenuseAsInt();
        if(ent.maxSpeed == 0)
            ent.maxSpeed = 4;
        ent.setId(id);
        entities.add(ent);
        entityHashMap.put(id, ent);
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

}
