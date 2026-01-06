package Sim;

import App.VCSApp;
import Sim.TDL.Message;
import Sim.TDL.RelayMsg;
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

    private HashMap<TDLReceiverComp, Entity> regesteredReceivers = new HashMap<>();
    public void processSendList2(){
        for (Message msg : toSendList){
            int transmitterRange = 0;
            Entity source = entityHashMap.get(msg.getSrcID());
            if(source.hasComponent(Component.ComponentType.TRANSMITTER)){
                transmitterRange = ((TDLTransmitterComp) source.getComponent(Component.ComponentType.TRANSMITTER)).getTransmitterRange();
            }
            Message message = null;

            for(TDLReceiverComp r : regesteredReceivers.keySet()){
                if (!r.parentEntity.getId().equals(msg.getSrcID())){
                    if (msg.type.equals(Message.MessageType.ENTITY_INFO) || msg.type.equals(Message.MessageType.KNOWN_INFO) || msg.type.equals(Message.MessageType.SURVEILLANCE_MSG)){
                        if (source.getPos().distance(r.parentEntity.getPos()) < transmitterRange){
                            message = msg.copy();
                            message.setTargetID(r.parentEntity.getId());
                            r.receiveMessage2(message);
                        }
                    } else {
                        if (msg.getTargetID().equals(r.parentEntity.getId())){
                            if (source.getPos().distance(r.parentEntity.getPos()) < transmitterRange){
                                r.receiveMessage2(msg);
                            } else relay2(msg);
                        }
                    }
                }
            }
            if (message != null){
                app.logPanel.toLog(message);
            } else app.logPanel.toLog(msg);
        }
        toSendList.clear();
    }

    public void registerReceiver(TDLReceiverComp rec, Entity owner){
        regesteredReceivers.put(rec, owner);
    }

    public void unregisterReceiver(TDLReceiverComp rec){
        regesteredReceivers.remove(rec);
    }

    public void relay2(Message msg){
        Entity targetReceiver = entityHashMap.get(msg.getTargetID());
        Entity temp = targetReceiver;
        Entity src = entityHashMap.get(msg.getSrcID());
        Entity nextRelay = targetReceiver;
        VCSApp app = msg.getApp();
        double posDiff = src.getPos().distance(targetReceiver.getPos());
        boolean isInRange = true;
        if(src.getComponent(Component.ComponentType.TRANSMITTER) == null)
            return;
        int srcTransmitterRange = ((TDLTransmitterComp) src.getComponent(Component.ComponentType.TRANSMITTER)).getTransmitterRange();

        ArrayList<Entity> inRangeEntities = new ArrayList<>();
        for (Entity entity : entities){
            if (src.getPos().distance(entity.getPos()) < srcTransmitterRange){
                if (src.getLocalWorld().getEntityHashMap().containsKey(entity.getId()) && entity.getSide() != Entity.Side.ENEMY)
                    inRangeEntities.add(entity);
            }
        }

        if(src.getLocalWorld().getEntityHashMap().containsKey(targetReceiver.getId())){
            while(src.getPos().distance(nextRelay.getPos()) > srcTransmitterRange && !inRangeEntities.isEmpty()){
                for(Entity e : inRangeEntities){
                    if (e.getSide().equals(Entity.Side.ENEMY)) continue;
                    if(e.getPos().distance(src.getPos()) < ((TDLTransmitterComp) e.getComponent(Component.ComponentType.TRANSMITTER)).getTransmitterRange()){
                        // HAS VISUAL ON TARGET
                        double newDiff = e.getPos().distance(nextRelay.getPos());
                        if(newDiff < posDiff){
                            posDiff = newDiff;
                            temp = e;
                        }
                    }
                }
                nextRelay = temp;
                if(nextRelay.equals(targetReceiver) && (src.getPos().distance(targetReceiver.getPos()) > srcTransmitterRange)){
                    break;
                }
                //counter++;
                //app.debugLog(String.format("Relay %d: %s\n", counter, targetReceiver.getName()));

            }

            if (msg.type.equals(Message.MessageType.RELAY)){
                ((TDLTransmitterComp) nextRelay.getComponent(Component.ComponentType.TRANSMITTER)).createRelayMessage(app, nextRelay.getId(), ((RelayMsg) msg).getRealMsg());
            } else  ((TDLTransmitterComp) nextRelay.getComponent(Component.ComponentType.TRANSMITTER)).createRelayMessage(app, nextRelay.getId(), msg);

        }

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

        ent.maxSpeed = ent.getSpeed().getMagnitudeAsInt();
        if(ent.maxSpeed == 0)
            ent.maxSpeed = 4;

        entities.add(ent);
        return ent;
    }

    public Entity createCommander(Vec2int pos, int range) {
        Entity ent = new Entity(this);
        ent.setName("HEADQUARTER");
        ent.setSide(Entity.Side.ALLY);
        if(pos == null) ent.setPos(new Vec2int( (int)(app.getWindow().getWidth() * 0.32), (int)(app.getWindow().getHeight() * 0.31)));
        else ent.setPos(pos);
        ent.setSpeed(new Vec2int(0, 0));
        ent.setType(Entity.Type.HQ);

        ent.maxSpeed = 0;

        entities.add(ent);
        ent.setId("HQ");

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

        ent.maxSpeed = ent.getSpeed().getMagnitudeAsInt();
        if(ent.maxSpeed == 0)
            ent.maxSpeed = 4;


        ent.setId(app.idManager.createId(ent));

        entities.add(ent);
        entityHashMap.put(ent.getId(), ent);

        if(eSide == Entity.Side.ALLY){
            ent.addComponent(new TDLReceiverComp(ent, entities ));
            ent.addComponent(new TDLTransmitterComp(ent, entities));
        }

        return ent;
    }

    public void update(int deltaTime) {
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
