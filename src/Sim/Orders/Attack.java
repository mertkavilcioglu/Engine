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

    public void attackEntity(Entity followerEntity, Entity targetEntity){
        if(targetEntity == null || followerEntity == null)
            return;
        double distX = targetEntity.getPos().x - followerEntity.getPos().x;
        double distY = targetEntity.getPos().y - followerEntity.getPos().y;
        double dist = followerEntity.getPos().distance(targetEntity.getPos());
        if(dist <= 3.0){
            System.out.format("%s reached the target. \n", followerEntity.getName());
            //follower.setSpeed(new Vec2int(0,0));
            destroy(targetEntity);
        }
        else{
            Vec2int newSpeed = followerEntity.getPos().vectorDiff(targetEntity.getPos()).normalize(4);
            followerEntity.setSpeed(newSpeed);
        }
    }

    public void destroy(Entity e){
        app.removeEntity(e);
    }
}
