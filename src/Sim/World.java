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
//    public Stack<Entity> createdEntities = new Stack<>();
//    public Stack<Entity> deletedEntities = new Stack<>();
//    public Stack<Entity> movedEntities = new Stack<>();
//    public Stack<Change> changes = new Stack<>();
    private Entity copiedEntity;

    public Stack<Entity> changedEntities = new Stack<>();
    public Stack<Entity> changes2 = new Stack<>();

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
        Radar r = new Radar(ent,entities);
        r.setRange(range);
        ent.addComponents(r);
        ent.setType(Entity.Type.HQ);

        ent.maxSpeed = 0;

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

    public void revert2() {
        if(changedEntities.isEmpty())
            return;
        changedEntities.pop().copyFrom(changes2.pop());
    }

//    public void revertLastChange(){
//        if(changes.isEmpty())
//            return;
//
//        if(changes.getLast().equals(Change.MOVE)){
//            if(!movedEntities.isEmpty()){
//                movedEntities.getLast().revertToPreviousPosition();
//                movedEntities.pop();
//                changes.pop();
//                app.mapView.repaint();
//            }
//        }
//        else if(changes.getLast().equals(Change.CREATE)){
//            if(!createdEntities.isEmpty()){
//                app.removeEntityInstantaneously(createdEntities.getLast());
//                createdEntities.pop();
//                changes.pop();
//            }
//        }
//        else if(changes.getLast().equals(Change.DELETE)){
//            if(!deletedEntities.isEmpty()){
//                Entity de = deletedEntities.getLast();
//                Entity newEnt;
//                if(de.hasComponent("Radar"))
//                    newEnt = app.createEntityByRevert(de.getName(), de.getSide(), de.getPos(), de.getSpeed(),
//                            ((Radar)de.getComponent("Radar")).getRange(), de.getType());
//                else
//                    newEnt = app.createEntityByRevert(de.getName(), de.getSide(), de.getPos(), de.getSpeed(), 0, de.getType());
//                while(!de.getPreviousPositions().isEmpty()){
//                    Vec2int prePos = new Vec2int(de.getPreviousPositions().getFirst().x, de.getPreviousPositions().getFirst().y);
//                    newEnt.getPreviousPositions().push(prePos);
//                    de.getPreviousPositions().removeFirst();
//                }
//                System.out.println(newEnt.getPreviousPositions().size());
//                deletedEntities.pop();
//                changes.pop();
//                while(createdEntities.contains(de))
//                    createdEntities.set(createdEntities.indexOf(de), newEnt);
//                while(movedEntities.contains(de))
//                    movedEntities.set(movedEntities.indexOf(de), newEnt);
//            }
//        }
//    }
//
//    public void clearAllStack(){
//        createdEntities.clear();
//        deletedEntities.clear();
//        movedEntities.clear();
//        changes.clear();
//    }

    public void clearAllStack(){
        changes2.clear();
        changedEntities.clear();
    }




}
