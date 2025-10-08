package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

import java.util.ArrayList;

public class TDLTransmitter {

    private Entity source;
    private ArrayList<Message> messagesToSend = new ArrayList<>();
    public TDLTransmitter(Entity source){
        this.source = source;
    }

    public void createMoveMessage(VCSApp app, Entity targetReceiver, Vec2int pos){
       MoveMsg moveMsg = new MoveMsg(app, source, targetReceiver, pos);
       //TODO: CALCULATE THE COUNTER BASED ON RANGE BETWEEN SRC & RECEIVER AND SET COUNTER
        moveMsg.setCounter(calculateRangeCounter(moveMsg));
        messagesToSend.add(moveMsg);
    }

    public void createAttackMessage(VCSApp app, Entity targetReceiver, Entity attackTarget){
        AttackMsg attackMsg = new AttackMsg(app, source, targetReceiver, attackTarget);
        //TODO: CALCULATE THE COUNTER BASED ON RANGE BETWEEN SRC & RECEIVER AND SET COUNTER
        attackMsg.setCounter(calculateRangeCounter(attackMsg));
        messagesToSend.add(attackMsg);
    }

    public void createFollowMessage(VCSApp app, Entity targetReceiver, Entity followEntity, int time){
        FollowMsg followMsg = new FollowMsg(app, source, targetReceiver, followEntity, time);
        //TODO: CALCULATE THE COUNTER BASED ON RANGE BETWEEN SRC & RECEIVER AND SET COUNTER
        followMsg.setCounter(calculateRangeCounter(followMsg));
        messagesToSend.add(followMsg);
    }

    public void sendMessage(Message msg, Entity receiver){
        receiver.getTdlReceiver().receiveMessage(msg);
    }

    public void update(){
        //TODO: range'e göre bir counter versin ve bu counter her update'de azalsın, counterı 0
        // olan mesajlar hedeflere sendlensin.
        if(!messagesToSend.isEmpty()){
            for(Message msg : messagesToSend){
                if(msg.getCounter() > 0)
                    msg.setCounter(msg.getCounter() - 1);
                else{
                    sendMessage(msg, msg.getReceiverEntity());
                    messagesToSend.remove(msg);
                }
            }
        }
    }

    public int calculateRangeCounter(Message msg){
        int counter;
        double diff = msg.getSrc().getPos().distance(msg.getReceiverEntity().getPos());
        counter = (int)(diff / msg.getReceiverEntity().getTdlReceiver().getRange());
        return counter;
    }

}
