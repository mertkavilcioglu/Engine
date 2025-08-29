package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

public class Move extends Order{

    private Vec2int destination;
    private boolean isExecute = false;

    public Move(VCSApp app, Entity src, Vec2int coordinates) {
        super(app, src);
        this.destination = coordinates;
    }


    public void moveTo(Vec2int destination){
        if(destination == null )
            return;
        double dist = source.getPos().distance(destination);
        if(dist <= 2.0){
            app.log(source.getName() + " reached the target. \n");
            source.setSpeed(new Vec2int(0,0));
            source.completeCurrentOrder();
            return;
        }
        else{
            updateSpeedToMovePosition(destination);
        }
    }

    public void updateSpeedToMovePosition(Vec2int destination){

        Vec2int newSpeed = new Vec2int();
        if(source.getPos().distance(destination) <= source.maxSpeed){
            newSpeed = new Vec2int(0,0);
            source.setPos(destination);
        }

        else
            newSpeed = source.getPos().vectorDiff(destination).normalize(source.maxSpeed);
        source.setSpeed(newSpeed);
    }

    @Override
    protected void printToLog(){
        if (!isExecute)
            app.log(source.getName() + " moving to " + destination);
        isExecute = true;
    }

    @Override
    protected void actualUpdate() {
        moveTo(destination);
        printToLog();
    }

    @Override
    public String createTextToPrint() {
        return String.format("Move %s\n", destination.toString());
    }

    @Override
    public String toString(){
        return String.format("Move order");
    }
}
