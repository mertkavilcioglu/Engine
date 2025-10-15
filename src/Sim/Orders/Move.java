package Sim.Orders;

import App.VCSApp;
import Sim.Entity;
import Sim.TDL.Message;
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
            source.getTdlTransmitter().createResultMessage(app, source, true);
            app.log(source.getName() + " reached the target.");
            source.setSpeed(new Vec2int(0,0));
            source.completeCurrentOrder();
            source.setCurrentOrderState(true);
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
        if (!isExecute){
            source.getTdlTransmitter().createReceiveMessage(app, source, Message.MessageType.MOVE_ORDER);
            app.log(source.getName() + " moving to " + destination);
            source.setCurrentOrderState(false);
        }
        isExecute = true;
    }

    @Override
    protected void actualUpdate() {
        moveTo(destination);
        printToLog();
    }

    @Override
    public String createTextToPrint() {
        return String.format("Move %s", destination.toString());
    }

    @Override
    public String toString(){
        return String.format("Move order");
    }
}
