package Sim;

import App.VCSApp;
import Vec.Vec2int;

import java.util.ArrayList;
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
    public Stack<Entity> latestCreatedEntities = new Stack<>();
    public Stack<Entity> latestDeletedEntities = new Stack<>();
    public Stack<Entity> latestMovedEntities = new Stack<>();
    public Stack<Change> latestChanges = new Stack<>();
    private Entity copiedEntity;

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

    public Entity createEntity(String name, Entity.Side side) {
        Entity ent = new Entity(this);
        ent.setName(name);
        ent.setSide(side);
        ent.setPos(Vec2int.getRandom(map.maxX / 8 ,map.maxY / 6));
        ent.setSpeed(Vec2int.getRandom(-4,4,-4,4));
        Radar r = new Radar(ent,entities);
        ent.addComponents(r);
        ent.setType(Entity.Type.AIR);

        ent.maxSpeed = ent.getSpeed().getHypotenuseAsInt();
        if(ent.maxSpeed == 0)
            ent.maxSpeed = 4;

        entities.add(ent);
        return ent;
    }

    public Entity createEntity(String eName, Entity.Side eSide, Vec2int pos, Vec2int speed, int range, Entity.Type type){
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


        //PixelColor color = new PixelColor(ent, entities);
        //color.CanMove(color.PixelColorFind(ent.getPos().x, ent.getPos().y, map));

        Radar r = new Radar(ent, entities);
        r.setRange(range);
        ent.addComponents(r);
        entities.add(ent);

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

    public enum Change {
        MOVE,
        CREATE,
        DELETE
    }

    public void revertLastChange(){
        if(latestChanges.isEmpty())
            return;

        if(latestChanges.getLast().equals(Change.MOVE)){
            if(!latestMovedEntities.isEmpty()){
                latestMovedEntities.getLast().revertToPreviousPosition();
                latestMovedEntities.pop();
                latestChanges.pop();
                app.mapView.repaint();
            }
        }
        else if(latestChanges.getLast().equals(Change.CREATE)){
            if(!latestCreatedEntities.isEmpty()){
                app.removeEntityInstantaneously(latestCreatedEntities.getLast());
                latestCreatedEntities.pop();
                latestChanges.pop();
            }
        }
        else if(latestChanges.getLast().equals(Change.DELETE)){
            if(!latestDeletedEntities.isEmpty()){
                Entity de = latestDeletedEntities.getLast();
                Entity newEnt;
                if(de.hasComponent("Radar"))
                    newEnt = app.createEntityByRevert(de.getName(), de.getSide(), de.getPos(), de.getSpeed(),
                            ((Radar)de.getComponent("Radar")).getRange(), de.getType());
                else
                    newEnt = app.createEntityByRevert(de.getName(), de.getSide(), de.getPos(), de.getSpeed(), 0, de.getType());
                while(!de.getPreviousPositions().isEmpty()){
                    Vec2int prePos = new Vec2int(de.getPreviousPositions().getFirst().x, de.getPreviousPositions().getFirst().y);
                    newEnt.getPreviousPositions().push(prePos);
                    de.getPreviousPositions().removeFirst();
                }
                System.out.println(newEnt.getPreviousPositions().size());
                latestDeletedEntities.pop();
                latestChanges.pop();
                while(latestCreatedEntities.contains(de))
                    latestCreatedEntities.set(latestCreatedEntities.indexOf(de), newEnt);
                while(latestMovedEntities.contains(de))
                    latestMovedEntities.set(latestMovedEntities.indexOf(de), newEnt);
            }
        }
    }

    public void clearAllStack(){
        latestCreatedEntities.clear();
        latestDeletedEntities.clear();
        latestMovedEntities.clear();
        latestChanges.clear();
    }
}
