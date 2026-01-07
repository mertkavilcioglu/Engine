package Sim.Orders;

import App.VCSApp;
import Sim.Component;
import Sim.Entity;
import Sim.TDL.TDLTransmitterComp;
import Vec.Vec2int;

public class Follow extends Order{

    private Entity targetEntity;
    private boolean isExecute = false;
    private int numOfUpdate = 0;
    private int followTime;

    public Follow(VCSApp app, Entity receiver, Entity sender, Entity target, int time) {
        super(app, receiver, sender, OrderType.FOLLOW);
        this.targetEntity = target;
        this.followTime = time;
        if (followTime == 0){
            String msgNotStarted = String.format("%s's follow order not started due to time problems.", this.receiver.getName());
            app.log(msgNotStarted);
            isExecute = true;
            this.receiver.completeCurrentOrder();
            this.receiver.setCurrentOrderState(true);
            orderFinish(this.receiver);
            return;
        }
    }

    Entity findEntity(String trgtname) {
        Entity entity = null;
        for (int i = 0; i < app.world.entities.size(); i++) {
            Entity e = app.world.entities.get(i);
            if (e.getName().equals(trgtname)) {
                entity = e;
            }
        }
        return entity;
    }


    public void followEntity(Entity target){
        double dist = receiver.getPos().distance(target.getPos());
        if (followTime > numOfUpdate){
            if(dist <= 25.0){
                if(targetEntity.isActive()){
                    int time = followTime - numOfUpdate;
                    receiver.setSpeed(targetEntity.getSpeed());
                    String reachString = String.format("%s has reached the target and will continue tracking for %d seconds.", receiver.getName(), time);
                    app.log(reachString);
                }
                else{
                    numOfUpdate = followTime;
                    if (receiver.getComponent(Component.ComponentType.TRANSMITTER) != null)
                        ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createResultMessage2(app, receiver, 1, OrderType.FOLLOW);
                    String reachString = String.format("%s has not found the target.", receiver.getName());
                    app.log(reachString);
                    if(receiver.getType() == Entity.Type.AIR)
                        receiver.setSpeed(new Vec2int(receiver.getSpeed().x/2, receiver.getSpeed().y/2));
                    else
                        receiver.setSpeed(new Vec2int(0,0));
                    receiver.completeCurrentOrder();
                    receiver.setCurrentOrderState(true);
                    orderFinish(receiver);
                }
                //source.setSpeed(new Vec2int(0,0));
            }
            else{

                Vec2int newSpeed;
                newSpeed = receiver.getPos().vectorDiff(targetEntity.getPos()).normalize(receiver.maxSpeed);
                if(newSpeed.getMagnitudeAsInt() < targetEntity.maxSpeed)
                    newSpeed = receiver.getPos().vectorDiff(targetEntity.getPos()).normalize( targetEntity.maxSpeed*2);
                receiver.setSpeed(newSpeed);
            }
        }
        else if (followTime == numOfUpdate){
            if(dist <= 25.0){
                if(targetEntity.isActive()){
                    if (receiver.getComponent(Component.ComponentType.TRANSMITTER) != null)
                        ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createResultMessage2(app, receiver, 0, OrderType.FOLLOW);
                    String reachString = String.format("%s has completed following the target.", receiver.getName());
                    app.log(reachString);
                    if(receiver.getType() == Entity.Type.AIR)
                        receiver.setSpeed(new Vec2int(receiver.getSpeed().x/2, receiver.getSpeed().y/2));
                    else
                        receiver.setSpeed(new Vec2int(0,0));
                }
                else{
                    if (receiver.getComponent(Component.ComponentType.TRANSMITTER) != null)
                        ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createResultMessage2(app, receiver, 1, OrderType.FOLLOW);
                    String reachString = String.format("%s has not found the target.", receiver.getName());
                    app.log(reachString);
                    if(receiver.getType() == Entity.Type.AIR)
                        receiver.setSpeed(new Vec2int(receiver.getSpeed().x/2, receiver.getSpeed().y/2));
                    else
                        receiver.setSpeed(new Vec2int(0,0));
                }
            }
            else{
                if (receiver.getComponent(Component.ComponentType.TRANSMITTER) != null)
                    ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createResultMessage2(app, receiver, 408, OrderType.FOLLOW);
                String timeOutString = String.format("%s stopped following the target %s because time was out.", receiver.getName(), target.getName());
                app.log(timeOutString);
                if(receiver.getType() == Entity.Type.AIR)
                    receiver.setSpeed(new Vec2int(receiver.getSpeed().x/2, receiver.getSpeed().y/2));
                else
                    receiver.setSpeed(new Vec2int(0,0));
            }
            receiver.completeCurrentOrder();
            receiver.setCurrentOrderState(true);
            orderFinish(receiver);
        }

    }

    @Override
    protected void printToLog(){
        String followString = String.format("%s is following %s.", receiver.getName(), targetEntity.getName());
        if (!isExecute){
            if (receiver.getComponent(Component.ComponentType.TRANSMITTER) != null)
                ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createMissionStartMessage2(app, receiver.getId(), "J13.2");
            app.log(followString);
            receiver.setCurrentOrderState(false);
        }
        isExecute = true;
    }

    @Override
    protected void actualUpdate() {
        numOfUpdate++;
        printToLog();
        followEntity(targetEntity);
    }

    @Override
    public String createTextToPrint() {
        return String.format("Follow %s", targetEntity.getName());
    }

    @Override
    public String toString(){
        return String.format("Follow order");
    }
}
