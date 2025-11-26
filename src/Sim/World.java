package Sim;

import App.VCSApp;
import Sim.TDL.Message;
import Sim.TDL.TDLReceiverComp;
import Sim.TDL.TDLTransmitterComp;
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

    private ArrayList<Message> toSendList = new ArrayList<>();
    public void send(Message msg) {
        toSendList.add(msg);
    }
    public void processSendList() {
        for (Message msg: toSendList) {
            // check ranges from source
            // put read q

            Entity source = entityHashMap.get(msg.getSrcID());
            int range = ((TDLTransmitterComp) source.getComponent(Component.ComponentType.TRANSMITTER)).getTransmitterRange();

            if (!source.isLocal()){
                if (!msg.getTargetID().equals(" ")){
                    String targetID = msg.getTargetID();
                    for(Entity e : source.w.entityHashMap.values()){
//                   if(e == source);
//                      //continue; //BURASI EMIR ALMA MESJLARINI BOZUYOR
                        if (e.getId().equals(targetID) && (source.getPos().distance(e.getPos()) < range) && !e.isLocal()) {
                            app.logPanel.toLog(msg);
                            ((TDLReceiverComp) e.getComponent(Component.ComponentType.RECEIVER)).receiveMessage2(msg);
                        }
                    }
                } else if (msg.type.equals(Message.MessageType.ENTITY_INFO)){
                    for (Entity e : source.w.entityHashMap.values()){
                        if ((source.getPos().distance(e.getPos()) < range) && !e.isLocal() && !e.equals(source)){
                            if (!source.getId().equals(e.getId()) && ((source.getId().equals("HQ") || source.getId().charAt(0) == 'A') && (e.getId().equals("HQ") || e.getId().charAt(0) == 'A'))) {
                                msg.setTargetID(e.getId());
                            } else continue;
                            ((TDLReceiverComp) e.getComponent(Component.ComponentType.RECEIVER)).receiveMessage2(msg);
                        }
                    }
                    msg.getApp().logPanel.toLog(msg);
                }
                else  if (msg.type.equals(Message.MessageType.KNOWN_INFO)){
                    for (Entity e : source.w.entityHashMap.values()){
                        if ((source.getPos().distance(e.getPos()) < range) && !e.isLocal() && !e.equals(source)){
                            if (!source.getId().equals(e.getId()) && ((source.getId().equals("HQ") || source.getId().charAt(0) == 'A') && (e.getId().equals("HQ") || e.getId().charAt(0) == 'A'))) {
                                msg.setTargetID(e.getId());
                            } else continue;
                            ((TDLReceiverComp) e.getComponent(Component.ComponentType.RECEIVER)).receiveMessage2(msg);
                        }
                    }
                    msg.getApp().logPanel.toLog(msg);
                }
            }
        }
    }

    private ArrayList<TDLReceiverComp> regesteredReceivers = new ArrayList<>();
    public void processSendList2(){
        for (Message msg : toSendList){
            int transmitterRange = 0;
            Entity source = entityHashMap.get(msg.getSrcID());
            if(source.hasComponent(Component.ComponentType.TRANSMITTER)){
                transmitterRange = ((TDLTransmitterComp) source.getComponent(Component.ComponentType.TRANSMITTER)).getTransmitterRange();
            }


            for(Entity entity : entities){
                if (source.getPos().distance(entity.getPos()) < transmitterRange){
                    for (TDLReceiverComp r : regesteredReceivers){
                        if (r.parentEntity.equals(entity)){
                            if (!entity.getId().equals(msg.getSrcID())){
                                if (msg.getTargetID().equals(" ")) {
                                    msg.setTargetID(entity.getId());
                                }
                                r.receiveMessage2(msg);
                            }
                        }
                    }
                }
            }
            app.logPanel.toLog(msg);
        }
        toSendList.clear();
    }

    public void registerReceiver(TDLReceiverComp rec){
        // TODO: Register the all receivers in the world and use these registers for processing
        regesteredReceivers.add(rec);
    }

    public Entity createEntity2(String name, Entity.Side side) {
        Entity ent = new Entity(this);
        ent.setName(name);
        ent.setSide(side);
        ent.setPos(new Vec2int(100,100));
        Vec2int speed = ThreadLocalRandom.current().nextInt() % 2 == 0 ? new Vec2int(4,4) : new Vec2int(-4,-4);
        ent.setSpeed(speed);
        Radar r = new Radar(ent,entities);
        ent.addComponent(r);
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
//        ent.setTransmitter(ent, entities);
//        ent.setReceiver(ent, entities);
        entityHashMap.put(ent.getId(), ent);
        ent.addComponent(new TDLReceiverComp(ent, entities ));
        ent.addComponent(new TDLTransmitterComp(ent, entities));
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


        ent.setId(app.idManager.createId(ent));

//        ent.setTransmitter(ent, entities);
//        ent.setReceiver(ent, entities);

        entities.add(ent);
        entityHashMap.put(ent.getId(), ent);

//        if(!ent.isLocal){
//            ent.setTransmitter(ent, entities);
//            ent.setReceiver(ent, entities);
//        }

        ent.addComponent(new TDLReceiverComp(ent, entities ));
        ent.addComponent(new TDLTransmitterComp(ent, entities));

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
