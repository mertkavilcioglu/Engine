package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

public class Attack extends Order{

    private Entity targetEntity;
    private boolean isExecute = false;
    private double dist;
    private Vec2int prevSpeed;
    private Vec2int targetPos;

    public Attack(VCSApp app, Entity src, Entity target) {
        super(app, src);
        this.targetEntity = target;
        //attackEntity(target);
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

    public void attackEntity(Entity targetEntity){
        if(targetEntity == null )
            return;
        if (targetEntity.isDetected()){
            targetPos = targetEntity.getPos();
        }
        dist = source.getPos().distance(targetPos);
        if(dist <= 4.0){
            if (targetEntity.isDetected()){
                String msgDestroy = String.format("%s destroy the target %s,", source.getName(), targetEntity.getName());
                app.log(msgDestroy);
                destroy(targetEntity);
            } else {
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

    @Override
    protected void printToLog(){
        if (!isExecute){
            String msgAttack = String.format("%s going to attack %s.", source.getName(), targetEntity.getName());
            app.log(msgAttack);
            source.setCurrentOrderState(false);
            prevSpeed = source.getSpeed();
        }
        isExecute = true;
    }

    @Override
    protected void actualUpdate() {
        printToLog();
        attackEntity(targetEntity);
    }

    @Override
    public String createTextToPrint() {
        return String.format("Attack %s", targetEntity.toString());
    }

    @Override
    public String toString(){
        return String.format("Attack order");
    }
}
