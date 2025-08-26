package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

public class Attack extends Order{

    private Entity targetEntity;
    private boolean isExecute = false;

    public Attack(VCSApp app, Entity src, Entity target) {
        super(app, src);
        this.targetEntity = target;
        //attackEntity(target);
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

    public void attackEntity(Entity targetEntity){
        if(targetEntity == null )
            return;
        double distX = targetEntity.getPos().x - source.getPos().x;
        double distY = targetEntity.getPos().y - source.getPos().y;
        double dist = source.getPos().distance(targetEntity.getPos());
        if(dist <= 4.0){
            app.log(source.getName() + " destroy the target " + targetEntity.getName());
            //follower.setSpeed(new Vec2int(0,0));
            destroy(targetEntity);

        }
        else{
            Vec2int newSpeed = source.getPos().vectorDiff(targetEntity.getPos()).normalize(8);
            source.setSpeed(newSpeed);
        }
    }

    public void destroy(Entity e){
        app.removeEntity(e);
        source.removeOrder();
        System.out.println("ORDER REMOVED");
    }

    public void printToLog(){
        if (!isExecute)
            app.log(source.getName() + " going to attack " + targetEntity.getName());
        isExecute = true;
    }

    @Override
    protected void actualUpdate() {
        attackEntity(targetEntity);
        printToLog();
    }

    @Override
    public String createTextToPrint() {
        return String.format("Attack %s\n", targetEntity.toString());
    }
}
