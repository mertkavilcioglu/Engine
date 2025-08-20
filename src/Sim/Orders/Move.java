package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import UI.Vec2intEditor;
import Vec.Vec2int;

public class Move extends Order{

    public Move(VCSApp app) {
        super(app);
    }

    public void moveTo(String unitName, Vec2int coordinates){
        Entity unit = app.follow.findEntity(unitName);
        double dist = unit.getPos().distance(coordinates);
        if(dist <= 2.0){
            app.log(unitName + " reached the target. \n");
            unit.setSpeed(new Vec2int(0,0));
            app.actionPanel.isMoving = false;
            return;
        }
        else{
            Vec2int newSpeed = unit.getPos().vectorDiff(coordinates).normalize(4);
            unit.setSpeed(newSpeed);
        }
    }
}
