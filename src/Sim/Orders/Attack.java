package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

public class Attack extends Order{

    public Attack(VCSApp app) {
        super(app);
    }

    Entity findEntity(String trgtname) {
        System.out.println("Attack: : findEntity function");
        Entity entity = null;
        for (int i = 0; i < app.world.entities.size(); i++) {
            Entity e = app.world.entities.get(i);
            if (e.getName().equals(trgtname)) {
                entity = e;
            }
        }
        return entity;
    }

    public void attackEntity(String followerName, String targetName){

        Entity follower = findEntity(followerName);
        Entity target = findEntity(targetName);
        if(target == null)
            return;
        double distX = target.getPos().x - follower.getPos().x;
        double distY = target.getPos().y - follower.getPos().y;
        double dist = follower.getPos().distance(target.getPos());
        if(dist <= 3.0){
            System.out.format("%s reached the target. \n", follower.getName());
            //follower.setSpeed(new Vec2int(0,0));
            destroy(target);
        }
        else{
            Vec2int newSpeed = follower.getPos().vectorDiff(target.getPos()).normalize(4);
            follower.setSpeed(newSpeed);
        }
    }

    public void destroy(Entity e){
        app.removeEntity(e);
    }
}
