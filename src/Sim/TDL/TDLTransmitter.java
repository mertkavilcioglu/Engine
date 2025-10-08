package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

import java.util.ArrayList;

public class TDLTransmitter {

    private Entity source;
    private ArrayList<Message> messagesToSend = new ArrayList<>();
    private ArrayList<Message> messagesToRemove = new ArrayList<>();
    public TDLTransmitter(Entity source){
        this.source = source;
    }

    public void createMoveMessage(VCSApp app, Entity targetReceiver, Vec2int pos){
       MoveMsg moveMsg = new MoveMsg(app, source, targetReceiver, pos);
        moveMsg.setCounter(calculateRangeCounter(moveMsg));
        messagesToSend.add(moveMsg);
        app.debugLog(String.format("Message sent from %s to %s", moveMsg.getSrc(), moveMsg.getReceiverEntity()));
    }

    public void createAttackMessage(VCSApp app, Entity targetReceiver, Entity attackTarget){
        AttackMsg attackMsg = new AttackMsg(app, source, targetReceiver, attackTarget);
        attackMsg.setCounter(calculateRangeCounter(attackMsg));
        messagesToSend.add(attackMsg);
        app.debugLog(String.format("Message sent from %s to %s", attackMsg.getSrc(), attackMsg.getReceiverEntity()));
    }

    public void createFollowMessage(VCSApp app, Entity targetReceiver, Entity followEntity, int time){
        FollowMsg followMsg = new FollowMsg(app, source, targetReceiver, followEntity, time);
        followMsg.setCounter(calculateRangeCounter(followMsg));
        messagesToSend.add(followMsg);
        app.debugLog(String.format("Message sent from %s to %s\n", followMsg.getSrc(), followMsg.getReceiverEntity()));
    }

    public void sendMessage(Message msg, Entity receiver){
        receiver.getTdlReceiver().receiveMessage(msg);
        msg.getApp().actionPanel.refreshCurrentOrderPanel();
        msg.getApp().debugLog("message received!!!");
    }

    public void update(){
        if(!messagesToSend.isEmpty()){
            for(Message msg : messagesToSend){
                if(msg.getCounter() > 0)
                    msg.setCounter(msg.getCounter() - 1);
                else{
                    sendMessage(msg, msg.getReceiverEntity());
                    messagesToRemove.add(msg);
                }
            }
        }
        if(!messagesToRemove.isEmpty()){
            messagesToSend.removeAll(messagesToRemove);
        }
    }

    public int calculateRangeCounter(Message msg){
        int counter;
        double diff = msg.getSrc().getPos().distance(msg.getReceiverEntity().getPos());
        counter = (int)(diff / msg.getReceiverEntity().getTdlReceiver().getRange());
        return counter;
    }

}
