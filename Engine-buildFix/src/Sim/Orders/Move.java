package Sim.Orders;

import App.VCSApp;
import Sim.Component;
import Sim.Entity;
import Sim.TDL.TDLTransmitterComp;
import Vec.Vec2int;

public class Move extends Order{

    private Vec2int destination;
    private boolean isExecute = false;

    public Move(VCSApp app, Entity receiver, Entity sender, Vec2int coordinates) {
        super(app, receiver, sender, OrderType.MOVE);
        this.destination = coordinates;
    }


    public void moveTo(Vec2int destination){
        if(destination == null )
            return;
        double dist = receiver.getPos().distance(destination);
        if(dist <= receiver.maxSpeed){
            if (receiver.getComponent(Component.ComponentType.TRANSMITTER) != null)
                ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createResultMessage2(app, receiver, 0, OrderType.MOVE);
            app.log(receiver.getName() + " reached the target.");
            if(receiver.getType() != Entity.Type.AIR)
                receiver.setSpeed(new Vec2int(0,0));
            else
                receiver.setSpeed(new Vec2int(receiver.getSpeed().x /2 , receiver.getSpeed().y / 2));
            receiver.setPos(destination);
            receiver.completeCurrentOrder();
            receiver.setCurrentOrderState(true);
            orderFinish(receiver);
            return;
        }
        else{
            updateSpeedToMovePosition(destination);
        }
    }

    public void updateSpeedToMovePosition(Vec2int destination){

        Vec2int newSpeed = new Vec2int();
        if(receiver.getPos().distance(destination) <= receiver.maxSpeed){
            if(receiver.getType() != Entity.Type.AIR){
                newSpeed = new Vec2int(0,0);
                receiver.setPos(destination);
                receiver.setSpeed(newSpeed);
            }
            else{
                receiver.setPos(destination);
                newSpeed = new Vec2int(receiver.getSpeed().x /2 , receiver.getSpeed().y / 2);
                receiver.setSpeed(newSpeed);
            }
        }
        else {
            newSpeed = receiver.getPos().vectorDiff(destination).normalize(receiver.maxSpeed);
            if(newSpeed.getMagnitudeAsInt() < 3)
                newSpeed = receiver.getPos().vectorDiff(destination).normalize(4);
            receiver.setSpeed(newSpeed);
        }

    }

    @Override
    protected void printToLog(){
        if (!isExecute){
            if (receiver.getComponent(Component.ComponentType.TRANSMITTER) != null)
                ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createMissionStartMessage2(app, receiver.getId(), "J13.3");
            app.log(receiver.getName() + " moving to " + destination);
            receiver.setCurrentOrderState(false);
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
