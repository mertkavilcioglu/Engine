package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

import java.util.List;

public class Follow extends Order{

    public Follow(VCSApp app, Entity e) {
        super(app, e);
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

    public void followEntity(String followerName, String targetName){

        Entity follower = findEntity(followerName);
        Entity target = findEntity(targetName);

        double distX = target.getPos().x - follower.getPos().x;
        double distY = target.getPos().y - follower.getPos().y;
        double dist = follower.getPos().distance(target.getPos());
        if(dist <= 5.0){
            System.out.format("%s reached the target. \n", follower.getName());
            follower.setSpeed(new Vec2int(0,0));
            return;
        }
        else{
            Vec2int newSpeed = follower.getPos().vectorDiff(target.getPos()).normalize(4);
            follower.setSpeed(newSpeed);
        }
    }

    @Override
    protected void actualUpdate() {

    }
}
