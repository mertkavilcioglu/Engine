package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

public class Follow extends Order{

    private Entity targetEntity;
    private boolean isExecute = false;
    private int numOfUpdate = 0;
    private int followTime;

    public Follow(VCSApp app, Entity src, Entity target, int time) {
        super(app, src);
        this.targetEntity = target;
        this.followTime = time;
        if (followTime == 0){
            app.log(source.getName() + "'s follow order not started due to time problems.\n");
            isExecute = true;
            source.completeCurrentOrder();
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
        double distX = target.getPos().x - source.getPos().x;
        double distY = target.getPos().y - source.getPos().y;
        double dist = source.getPos().distance(target.getPos());
        if (followTime > numOfUpdate){
            if(dist <= 3.0){
                int time = followTime - numOfUpdate;
                String reachString = String.format("%s has reached the target and will continue tracking for %d seconds. \n", source.getName(), time);
                app.log(reachString);
                //source.setSpeed(new Vec2int(0,0));
            }
            else{
                int targetSpeed;
                if(source.maxSpeed <= targetEntity.maxSpeed)
                    targetSpeed = targetEntity.maxSpeed*2;
                else
                    targetSpeed = source.maxSpeed*2;
                Vec2int newSpeed = source.getPos().vectorDiff(target.getPos()).normalize(targetSpeed);
                source.setSpeed(newSpeed);
            }
        }
        else if (followTime == numOfUpdate){
            if(dist <= 3.0){
                String reachString = String.format("%s has reached the target. \n", source.getName());
                app.log(reachString);
                source.setSpeed(new Vec2int(0,0));
                source.completeCurrentOrder();
                return;
            }
            else{
                String timeOutString = String.format("%s stopped following the target %s because time was out. \n", source.getName(), target.getName());
                app.log(timeOutString);
                source.completeCurrentOrder();
                return;
            }
        }

    }

    @Override
    protected void printToLog(){
        String followString = String.format("%s is following %s. \n",source.getName(), targetEntity.getName());
        if (!isExecute)
            app.log(followString);
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
        return String.format("Follow %s\n", targetEntity.getName());
    }

    @Override
    public String toString(){
        return String.format("Follow order");
    }
}
