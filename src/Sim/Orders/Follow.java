package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import Sim.TDL.Message;
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
        receiver.getTdlTransmitter2().createReceiveMessage2(app, receiver, Message.MessageType.FOLLOW_ORDER);
        if (followTime == 0){
            String msgNotStarted = String.format("%s's follow order not started due to time problems.", this.receiver.getName());
            app.log(msgNotStarted);
            isExecute = true;
            this.receiver.completeCurrentOrder();
            this.receiver.setCurrentOrderState(true);
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
        double distX = target.getPos().x - receiver.getPos().x;
        double distY = target.getPos().y - receiver.getPos().y;
        double dist = receiver.getPos().distance(target.getPos());
        if (followTime > numOfUpdate){
            if(dist <= 3.0){
                int time = followTime - numOfUpdate;

                String reachString = String.format("%s has reached the target and will continue tracking for %d seconds.", receiver.getName(), time);
                app.log(reachString);
                //source.setSpeed(new Vec2int(0,0));
            }
            else{
                int targetSpeed;
                if(receiver.maxSpeed <= targetEntity.maxSpeed)
                    targetSpeed = targetEntity.maxSpeed*2;
                else
                    targetSpeed = receiver.maxSpeed*2;
                Vec2int newSpeed = receiver.getPos().vectorDiff(target.getPos()).normalize(targetSpeed);
                receiver.setSpeed(newSpeed);
            }
        }
        else if (followTime == numOfUpdate){
            if(dist <= 3.0){
                receiver.getTdlTransmitter2().createResultMessage2(app, receiver, true, OrderType.FOLLOW);
                String reachString = String.format("%s has reached the target.", receiver.getName());
                app.log(reachString);
                receiver.setSpeed(new Vec2int(0,0));
            }
            else{
                receiver.getTdlTransmitter2().createResultMessage2(app, receiver, false, OrderType.FOLLOW);
                String timeOutString = String.format("%s stopped following the target %s because time was out.", receiver.getName(), target.getName());
                app.log(timeOutString);
            }
            receiver.completeCurrentOrder();
            receiver.setCurrentOrderState(true);
        }

    }

    @Override
    protected void printToLog(){
        String followString = String.format("%s is following %s.", receiver.getName(), targetEntity.getName());
        if (!isExecute){
            receiver.getTdlTransmitter2().createMissionStartMessage2(app, receiver.getId(), "J13.2");
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
