package Sim.Orders;

import App.VCSApp;
import Sim.Component;
import Sim.Entity;
import Sim.TDL.Message;
import Sim.TDL.TDLTransmitterComp;
import Vec.Vec2int;

public class Attack extends Order{

    private String attackTargetID;
    private boolean isExecute = false;
    private double dist;
    private Vec2int prevSpeed;
    private Vec2int targetPos;
    private Vec2int lastKnownPos = null;
    private Entity currentAttackTarget;
    private int finishStat;

    public Attack(VCSApp app, Entity receiver, Entity sender, String  attackTargetID) {
        super(app, receiver, sender, OrderType.ATTACK);
        this.attackTargetID = attackTargetID;
        //attackEntity(target);
    }

    public void attackEntity(Entity targetEntity){
        if(targetEntity == null || !targetEntity.isActive()){
            if(targetPos != null || lastKnownPos != null){
                double prevDist = receiver.getPos().distance(targetPos);
                if(prevDist <= 4.0){
                    if (receiver.getComponent(Component.ComponentType.TRANSMITTER) != null)
                        ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createResultMessage2(app, receiver, 404, OrderType.ATTACK);
                    this.finishStat = 404;
                    String notFoundMsg = String.format("%s not found at the last location by %s.", currentAttackTarget.getName(), receiver.getName());
                    app.log(notFoundMsg);
                    if(receiver.getType() == Entity.Type.AIR)
                        receiver.setSpeed(new Vec2int(-(receiver.getSpeed().x/2) , -(receiver.getSpeed().y/2)));
                    else
                        receiver.setSpeed(new Vec2int(0,0));
                    receiver.completeCurrentOrder();
                    receiver.setCurrentOrderState(true);
                    finish(receiver);
                    app.mapView.setTargetPos(null);
                    targetPos = null;
                }
                else{
                    Vec2int newSpeed = new Vec2int();
                    if(receiver.getPos().distance(targetPos) <= receiver.maxSpeed){
                        if(receiver.getType() != Entity.Type.AIR){
                            newSpeed = new Vec2int(0,0);
                            receiver.setPos(targetPos);
                            receiver.setSpeed(newSpeed);
                        }
                        else{
                            receiver.setPos(targetPos);
                            newSpeed = new Vec2int(receiver.getSpeed().x /2 , receiver.getSpeed().y / 2);
                            if(newSpeed.getMagnitudeAsInt() < 3)
                                newSpeed = receiver.getPos().vectorDiff(targetPos).normalize(4);
                            receiver.setSpeed(newSpeed);
                        }
                    }
                    else {
                        newSpeed = receiver.getPos().vectorDiff(lastKnownPos).normalize(receiver.maxSpeed);
                        if(newSpeed.getMagnitudeAsInt() < 3)
                            newSpeed = receiver.getPos().vectorDiff(lastKnownPos).normalize(4);
                        receiver.setSpeed(newSpeed);
                    }
                }
            }
                return;
        }
        currentAttackTarget = targetEntity;
        lastKnownPos = targetEntity.getPos();
        //if (targetEntity.isDetected()){
        targetPos = receiver.getLocalWorld().getEntityHashMap().get(targetEntity.getId()).getPos();
        //}
        dist = receiver.getPos().distance(targetPos);
        app.mapView.setTargetPos(targetPos);
        if(dist <= 20.0){
            if (receiver.getLocalWorld().getEntities().contains(targetEntity)){
                if (receiver.getComponent(Component.ComponentType.TRANSMITTER) != null)
                    ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createResultMessage2(app, receiver, 0, OrderType.ATTACK);
                this.finishStat = 0;
                String msgDestroy = String.format("%s destroyed the target %s,", receiver.getName(), targetEntity.getName());
                if(receiver.getType() != Entity.Type.AIR)
                    receiver.setSpeed(new Vec2int(0,0));
                app.log(msgDestroy);
                destroy(targetEntity);
                app.mapView.setTargetPos(null);
                targetPos = null;
            } else {
                if (receiver.getComponent(Component.ComponentType.TRANSMITTER) != null)
                    ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createResultMessage2(app, receiver, 404, OrderType.ATTACK);
                this.finishStat = 404;
                String notFoundMsg = String.format("%s not found at the last location by %s.", targetEntity.getName(), receiver.getName());
                app.log(notFoundMsg);
                if(receiver.getType() == Entity.Type.AIR)
                    receiver.setSpeed(new Vec2int(receiver.getSpeed().x/-2 , receiver.getSpeed().y/-2));
                else
                    receiver.setSpeed(new Vec2int(0,0));
                receiver.completeCurrentOrder();
                receiver.setCurrentOrderState(true);
                finish(receiver);
                app.mapView.setTargetPos(null);
                targetPos = null;
            }


        }
        else{
            Vec2int newSpeed = new Vec2int();
            if(receiver.getPos().distance(targetEntity.getPos()) <= receiver.maxSpeed){
                if(receiver.getType() != Entity.Type.AIR){
                    newSpeed = new Vec2int(0,0);
                    receiver.setPos(targetEntity.getPos());
                    receiver.setSpeed(newSpeed);
                }
                else{
                    receiver.setPos(targetEntity.getPos());
                    newSpeed = new Vec2int(receiver.getSpeed().x /2 , receiver.getSpeed().y / 2);
                    receiver.setSpeed(newSpeed);
                }
            }
            else {
                newSpeed = receiver.getPos().vectorDiff(targetEntity.getPos()).normalize(receiver.maxSpeed);
                if(newSpeed.getMagnitudeAsInt() < targetEntity.maxSpeed)
                    newSpeed = receiver.getPos().vectorDiff(targetEntity.getPos()).normalize(targetEntity.maxSpeed*2);
                receiver.setSpeed(newSpeed);
            }
        }
    }

    public void destroy(Entity e){
        app.removeEntity(e);
        receiver.completeCurrentOrder();
        receiver.setCurrentOrderState(true);
        finish(receiver);
    }

    public String  getAttackTargetID(){
        return attackTargetID;
    }

    @Override
    protected void printToLog(){
        if (!isExecute){
            if (receiver.getComponent(Component.ComponentType.TRANSMITTER) != null)
                ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createMissionStartMessage2(app, receiver.getId(), "J13.1");
            String msgAttack = String.format("%s going to attack %s.", receiver.getName(), attackTargetID);
            app.log(msgAttack);
            receiver.setCurrentOrderState(false);
            prevSpeed = receiver.getSpeed();
        }
        isExecute = true;
    }

    public int getFinishStat() {
        return finishStat;
    }

    @Override
    protected void actualUpdate() {
        printToLog();
        attackEntity(receiver.getLocalWorld().getEntityHashMap().get(attackTargetID));
    }

    @Override
    public String createTextToPrint() {
        return String.format("Attack %s", attackTargetID);
    }

    @Override
    public String toString(){
        return String.format("Attack order");
    }
}
