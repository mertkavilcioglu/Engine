package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

public class Follow extends Order{

    private Entity targetEntity;

    public Follow(VCSApp app, Entity src, Entity target) {
        super(app, src);
        this.targetEntity = target;
        //followEntity(target);
    }

    Entity findEntity(String trgtname) {
        System.out.println("Follow: : findEntity function");
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
        if(dist <= 3.0){
            System.out.format("%s reached the target. \n", source.getName());
            source.setSpeed(new Vec2int(0,0));
            return;
        }
        else{
            Vec2int newSpeed = source.getPos().vectorDiff(target.getPos()).normalize(4);
            source.setSpeed(newSpeed);
        }
    }

    @Override
    protected void actualUpdate() {
        followEntity(targetEntity);
    }

    @Override
    public String createTextToPrint() {
        return String.format("Follow %s\n", targetEntity.getName());
    }
}
