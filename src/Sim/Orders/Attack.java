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

    public Attack(VCSApp app, Entity source, String  attackTargetID) {
        super(app, source);
        this.attackTargetID = attackTargetID;
        //attackEntity(target);
    }

    public void attackEntity(Entity targetEntity){
        if(targetEntity == null )
            return;
        if (targetEntity.isDetected()){
            targetPos = targetEntity.getPos();
        }
        dist = source.getPos().distance(targetPos);
        if(dist <= 4.0){
            if (targetEntity.isDetected()){
                source.getTdlTransmitter().createResultMessage(app, source, true);
                String msgDestroy = String.format("%s destroy the target %s,", source.getName(), targetEntity.getName());
                app.log(msgDestroy);
                destroy(targetEntity);
            } else {
                source.getTdlTransmitter().createResultMessage(app, source, false);
                String notFoundMsg = String.format("%s not found at the last location by %s.", targetEntity.getName(), source.getName());
                app.log(notFoundMsg);
                //TODO order bitince ya da yarım kalınca unitlere hareket belirleme
                source.setSpeed(new Vec2int(0,0));
                source.completeCurrentOrder();
                source.setCurrentOrderState(true);
            }


        }
        else{
                int targetSpeed;
                if(source.maxSpeed <= targetEntity.maxSpeed)
                    targetSpeed = targetEntity.maxSpeed*2;
                else
                    targetSpeed = source.maxSpeed*2;


                Vec2int newSpeed = new Vec2int();
                if(source.getPos().distance(targetPos) <= source.maxSpeed){
                    newSpeed = new Vec2int(0,0);
                    source.setPos(targetEntity.getPos());
                }
                else{
                    newSpeed = source.getPos().vectorDiff(targetPos).normalize(targetSpeed);
                    source.setSpeed(newSpeed);
                }
        }
    }

    public void destroy(Entity e){
        app.removeEntity(e);
        source.completeCurrentOrder();
        source.setCurrentOrderState(true);
    }

    public String  getAttackTargetID(){
        return attackTargetID;
    }

    @Override
    protected void printToLog(){
        if (!isExecute){
            source.getTdlTransmitter().createReceiveMessage(app, source, Message.MessageType.ATTACK_ORDER);
            String msgAttack = String.format("%s going to attack %s.", source.getName(), attackTargetID);
            app.log(msgAttack);
            source.setCurrentOrderState(false);
            prevSpeed = source.getSpeed();
        }
        isExecute = true;
    }

    @Override
    protected void actualUpdate() {
        printToLog();
        attackEntity(source.getLocalWorld().getEntityHashMap().get(attackTargetID));
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
