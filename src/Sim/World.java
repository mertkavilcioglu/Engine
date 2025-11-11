package Sim;

import App.VCSApp;
import Vec.Vec2int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class World{
    public final VCSApp app;

    public World(VCSApp app) {
        this.app = app;
    }
    public WorldMap map = new WorldMap();

    public ArrayList<Entity> entities = new ArrayList<>();
    public ArrayList<Entity> entitiesToRemove = new ArrayList<>();
    public HashMap<String, Entity> entityHashMap = new HashMap<>();
    private Entity copiedEntity;

    public Stack<Entity> changedEntities = new Stack<>();
    public Stack<Entity> changes = new Stack<>();

        public Entity createEntity2(String name, Entity.Side side) {
        Entity ent = new Entity(this);
        ent.setName(name);
        ent.setSide(side);
        ent.setPos(new Vec2int(100,100));
        Vec2int speed = ThreadLocalRandom.current().nextInt() % 2 == 0 ? new Vec2int(4,4) : new Vec2int(-4,-4);
        ent.setSpeed(speed);
        Radar r = new Radar(ent,entities);
        ent.addComponents(r);
        ent.setType(Entity.Type.AIR);

        ent.maxSpeed = ent.getSpeed().getHypotenuseAsInt();
        if(ent.maxSpeed == 0)
            ent.maxSpeed = 4;

        entities.add(ent);
        return ent;
    }

    public Entity createCommander(Vec2int pos, int range) {
        Entity ent = new Entity(this);
        ent.setName("HEADQUARTER");
        ent.setSide(Entity.Side.ALLY);
        if(pos == null) ent.setPos(new Vec2int(500, 250));
        else ent.setPos(pos);
        ent.setSpeed(new Vec2int(0, 0));
        ent.setType(Entity.Type.HQ);

        ent.maxSpeed = 0;

        entities.add(ent);
        ent.setId("HQ");
        entityHashMap.put(ent.getId(), ent);
        return ent;
    }

    public Entity createEntity(String eName, Entity.Side eSide, Vec2int pos, Vec2int speed, Entity.Type type){
        if(eName == null){
            return null;
        }
        Entity ent = new Entity(this);
        ent.setName(eName);
        ent.setSide(eSide);

        ent.setPos(new Vec2int(pos.x, pos.y));
        ent.setSpeed(new Vec2int(speed.x, speed.y));

        ent.setType(type);

        ent.maxSpeed = ent.getSpeed().getHypotenuseAsInt();
        if(ent.maxSpeed == 0)
            ent.maxSpeed = 4;

        entities.add(ent);
        entityHashMap.put(ent.getId(), ent);

        return ent;
    }

    public void update(int deltaTime) {
        //System.out.println("World::update - THREAD : " + Thread.currentThread().getName());
        //System.out.println("Sim.World::update");
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).update(deltaTime);
        }
    }
    public void render() {
        System.out.println("World::render");
    }

    public Entity getCopiedEntity(){
        return copiedEntity;
    }

    public void setCopiedEntity(Entity e){
        copiedEntity = e;
    }

    public HashMap<String, Entity> getEntityHashMap(){
            return entityHashMap;
    }

    public void revert() {
        if(changedEntities.isEmpty())
            return;
        changedEntities.pop().copyFrom(changes.pop());
    }

    public void clearAllStack(){
        changes.clear();
        changedEntities.clear();
    }

}
