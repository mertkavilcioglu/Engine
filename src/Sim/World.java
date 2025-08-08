package Sim;

import Vec.Vec2int;

import java.util.ArrayList;
import java.util.UUID;

public class World{
    public WorldMap map = new WorldMap();
    public ArrayList<Entity> entities = new ArrayList<>();

    public Entity createEntity(String name, int side) {
        Entity ent = new Entity(this);
        ent.setName(name);
        ent.setSide(side);
        ent.setPos(Vec2int.getRandom(map.maxX / 8 ,map.maxY / 6));
        ent.setSpeed(Vec2int.getRandom(0,4,0,4));
        Radar r = new Radar(ent,entities);
        ent.addComponents(r);
        entities.add(ent);
        return ent;
    }

    public Entity createEntity(String eName, int eSide, Vec2int pos, Vec2int speed){
        if(eName == null){
            return null;
        }
        System.out.format("Created entity %s with x:%d and y:%d", eName, pos.x, pos.y);
        Entity ent = new Entity(this);
        String uniqueID = UUID.randomUUID().toString();
        ent.setId(uniqueID);
        ent.setName(eName);
        ent.setSide(eSide);

        ent.setPos(new Vec2int(pos.x, pos.y));
        ent.setSpeed(new Vec2int(speed.x, speed.y));

        Radar r = new Radar(ent, entities);
        ent.addComponents(r);
        entities.add(ent);

        return ent;
    }

    public void update(int deltaTime) {
        System.out.println(Thread.currentThread().getName());
        //System.out.println("Sim.World::update");
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).update(deltaTime);
        }
    }
    public void render() {
        System.out.println("World::render");
    }
}
