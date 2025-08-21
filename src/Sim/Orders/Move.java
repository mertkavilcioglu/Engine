package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import UI.Vec2intEditor;
import Vec.Vec2int;

public class Move extends Order{

    public Move(VCSApp app, Entity e) {

        super(app, e);
    }

    public void moveTo(Vec2int coordinates){
        double dist = source.getPos().distance(coordinates);
        if(dist <= 2.0){
            app.log(source.getName() + " reached the target. \n");
            source.setSpeed(new Vec2int(0,0));
            removeOrder();
            app.actionPanel.isMoving = false;
            return;
        }
        else{
            Vec2int newSpeed = source.getPos().vectorDiff(coordinates).normalize(4);
            source.setSpeed(newSpeed);
        }
    }

    @Override
    protected void actualUpdate() {

    }
}
