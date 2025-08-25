package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import UI.Vec2intEditor;
import Vec.Vec2int;

public class Move extends Order{

    private Vec2int destination;
    private boolean isExecute = false;

    public Move(VCSApp app, Entity src, Vec2int coordinates) {

        super(app, src);
        this.destination = coordinates;
    }

    public void printToLog(){
        if (!isExecute)
            app.log(source.getName() + " moving to " + destination);
        isExecute = true;
    }

    public void moveTo(Vec2int destination){
        if(destination == null )
            return;
        double dist = source.getPos().distance(destination);
        if(dist <= 2.0){
            app.log(source.getName() + " reached the target. \n");
            source.setSpeed(new Vec2int(0,0));
            source.removeOrder();
            return;
        }
        else{
            findSpeed(destination);
        }
    }

    public void findSpeed(Vec2int destination){
        Vec2int newSpeed = source.getPos().vectorDiff(destination).normalize(4);
        source.setSpeed(newSpeed);
    }

    @Override
    protected void actualUpdate() {
        moveTo(destination);
        printToLog();
    }
}
