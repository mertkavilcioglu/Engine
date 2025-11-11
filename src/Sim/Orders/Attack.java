package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import Sim.TDL.Message;
import Vec.Vec2int;

public class Attack extends Order{

    private String attackTargetID;
    private boolean isExecute = false;
    private double dist;
    private Vec2int prevSpeed;
    private Vec2int targetPos;

    public Attack(VCSApp app, Entity receiver, Entity sender, String  attackTargetID) {
        super(app, receiver, sender);
        this.attackTargetID = attackTargetID;
        //attackEntity(target);
    }

    public void attackEntity(Entity targetEntity){
        if(targetEntity == null )
            return;
        //if (targetEntity.isDetected()){
        targetPos = receiver.getLocalWorld().getEntityHashMap().get(targetEntity.getId()).getPos();
        //}
        dist = receiver.getPos().distance(targetPos);
        if(dist <= 4.0){
            if (receiver.getLocalWorld().getEntities().contains(targetEntity)){
                receiver.getTdlTransmitter().createResultMessage(app, receiver, true);
                String msgDestroy = String.format("%s destroy the target %s,", receiver.getName(), targetEntity.getName());
                app.log(msgDestroy);
                destroy(targetEntity);
            } else {
                receiver.getTdlTransmitter().createResultMessage(app, receiver, false);
                String notFoundMsg = String.format("%s not found at the last location by %s.", targetEntity.getName(), receiver.getName());
                app.log(notFoundMsg);
                //TODO order bitince ya da yarım kalınca unitlere hareket belirleme
                    receiver.setSpeed(new Vec2int(0,0));
                receiver.completeCurrentOrder();
                receiver.setCurrentOrderState(true);
            }


        }
        else{
                int targetSpeed;
                if(receiver.maxSpeed <= targetEntity.maxSpeed)
                    targetSpeed = targetEntity.maxSpeed*2;
                else
                    targetSpeed = receiver.maxSpeed*2;


                Vec2int newSpeed = new Vec2int();
                if(receiver.getPos().distance(targetPos) <= receiver.maxSpeed){
                    newSpeed = new Vec2int(0,0);
                    receiver.setPos(targetEntity.getPos());
                }
                else{
                    newSpeed = receiver.getPos().vectorDiff(targetPos).normalize(targetSpeed);
                    receiver.setSpeed(newSpeed);
                }
        }
    }

    public void destroy(Entity e){
        app.removeEntity(e);
        receiver.completeCurrentOrder();
        receiver.setCurrentOrderState(true);
    }

    public String  getAttackTargetID(){
        return attackTargetID;
    }

    @Override
    protected void printToLog(){
        if (!isExecute){
            receiver.getTdlTransmitter().createReceiveMessage(app, receiver, Message.MessageType.ATTACK_ORDER);
            String msgAttack = String.format("%s going to attack %s.", receiver.getName(), attackTargetID);
            app.log(msgAttack);
            receiver.setCurrentOrderState(false);
            prevSpeed = receiver.getSpeed();
        }
        isExecute = true;
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
