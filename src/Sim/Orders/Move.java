package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import UI.Vec2intEditor;
import Vec.Vec2int;

public class Move extends Order{

    private Vec2int destination;
    public Move(VCSApp app, Entity src, Vec2int coordinates) {

        super(app, src);
        this.destination = coordinates;
        moveTo(destination);
    }

    public void moveTo(Vec2int destination){
        double dist = source.getPos().distance(destination);
        if(dist <= 2.0){
            app.log(source.getName() + " reached the target. \n");
            source.setSpeed(new Vec2int(0,0));
            source.removeOrder();
            return;
        }
        else{
            Vec2int newSpeed = source.getPos().vectorDiff(destination).normalize(4);
            source.setSpeed(newSpeed);
        }
    }

    @Override
    protected void actualUpdate() {

    }
}
