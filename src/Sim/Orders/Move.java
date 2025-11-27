package Sim.Orders;

import App.VCSApp;
import Sim.Component;
import Sim.Entity;
import Sim.TDL.Message;
import Sim.TDL.TDLTransmitterComp;
import Vec.Vec2int;

public class Move extends Order{

    private Vec2int destination;
    private boolean isExecute = false;

    public Move(VCSApp app, Entity receiver, Entity sender, Vec2int coordinates) {
        super(app, receiver, sender, OrderType.MOVE);
        this.destination = coordinates;
        ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(app, receiver, Message.MessageType.MOVE_ORDER);
    }


    public void moveTo(Vec2int destination){
        if(destination == null )
            return;
        double dist = receiver.getPos().distance(destination);
        if(dist <= 2.0){
            ((TDLTransmitterComp) receiver.getComponent(Component.ComponentType.TRANSMITTER)).createResultMessage2(app, receiver, true, OrderType.MOVE);
            app.log(receiver.getName() + " reached the target.");
            receiver.setSpeed(new Vec2int(0,0));
            receiver.completeCurrentOrder();
            receiver.setCurrentOrderState(true);
            finish(receiver);
            return;
        }
        else{
            updateSpeedToMovePosition(destination);
        }
    }

    public void updateSpeedToMovePosition(Vec2int destination){

        Vec2int newSpeed = new Vec2int();
        if(receiver.getPos().distance(destination) <= receiver.maxSpeed){
            newSpeed = new Vec2int(0,0);
            receiver.setPos(destination);
        }

        else
            newSpeed = receiver.getPos().vectorDiff(destination).normalize(receiver.maxSpeed);
        receiver.setSpeed(newSpeed);
    }

    @Override
    protected void printToLog(){
        if (!isExecute){
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
