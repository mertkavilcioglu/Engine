package Sim;

import App.VCSapp;
import UI.VCSpanel;
import Vec.Vec2int;

import java.util.ArrayList;

public class World{
    public WorldMap map = new WorldMap();
    public ArrayList<Entity> entities = new ArrayList<>(); //TODO static kalmadan yapmaya calis

    public Entity createEntity(String name) {
        Entity ent = new Entity();
        ent.setName(name);
        ent.setPos(Vec2int.getRandom(map.maxX / 8 ,map.maxY / 6));
        ent.setSpeed(Vec2int.getRandom(0,4,0,4));
        Radar r = new Radar(ent,entities);
        ent.addComponents(r);
        //entities.add(ent);
        return ent;
    }

    public Entity createEntity(String eName, Vec2int pos, Vec2int speed){
        if(eName == null){
            //System.out.println("NAME IS NULL");
            return null;
        }

        System.out.format("Created entity %s with x:%d and y:%d", eName, pos.x, pos.y);
        Entity ent = new Entity();
        ent.setName(eName);

        ent.setPos(new Vec2int(pos.x, pos.y));
        //u.speed = Vec.Vec2int.getRandom(0,4,0,4);
        ent.setSpeed(new Vec2int(speed.x, speed.y));

        Radar r = new Radar(ent, entities);
        ent.addComponents(r);

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
