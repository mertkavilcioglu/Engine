package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

import java.util.ArrayList;
import java.util.List;

public class TDLTransmitter {

    private Entity source;
    private int range = 200;

    private ArrayList<Message> messagesToSend = new ArrayList<>();
    private ArrayList<Message> messagesToRemove = new ArrayList<>();
    public TDLTransmitter(Entity source){
        this.source = source;
    }

    public void createMoveMessage(VCSApp app, Entity targetReceiver, Vec2int pos){
       MoveMsg moveMsg = new MoveMsg(app, VCSApp.headQuarter, targetReceiver, pos);
        moveMsg.setCounter(calculateRangeCounter(moveMsg));
        messagesToSend.add(moveMsg);
        app.debugLog(String.format("Message sent from %s to %s", moveMsg.getSrc(), moveMsg.getReceiverEntity()));
    }

    public void createAttackMessage(VCSApp app, Entity targetReceiver, Entity attackTarget){
        AttackMsg attackMsg = new AttackMsg(app, VCSApp.headQuarter, targetReceiver, attackTarget);
        attackMsg.setCounter(calculateRangeCounter(attackMsg));
        messagesToSend.add(attackMsg);
        app.debugLog(String.format("Message sent from %s to %s", attackMsg.getSrc(), attackMsg.getReceiverEntity()));
    }

    public void createFollowMessage(VCSApp app, Entity targetReceiver, Entity followEntity, int time){
        FollowMsg followMsg = new FollowMsg(app, VCSApp.headQuarter, targetReceiver, followEntity, time);
        followMsg.setCounter(calculateRangeCounter(followMsg));
        messagesToSend.add(followMsg);
        app.debugLog(String.format("Message sent from %s to %s\n", followMsg.getSrc(), followMsg.getReceiverEntity()));
    }

    public void createInfoMessage(VCSApp app, Entity source, List<Entity> targetReceivers){
        //TODO nasıl createlenip ne şekilde ne zaman basılcağına bakmalı
        InfoMsg infoMsg = new InfoMsg(app, source, targetReceivers);
        messagesToSend.add(infoMsg);
        for (Entity entity : targetReceivers){
            //app.debugLog(String.format("Message sent from %s to %s\n", infoMsg.getSrc(), entity));
        }
    }

    public void createReceiveMessage(VCSApp app, Entity source, Message.MessageType type){
        ReceiveMsg receiveMsg = new ReceiveMsg(app, source, VCSApp.headQuarter, type);
        receiveMsg.setCounter(calculateRangeCounter(receiveMsg));
        messagesToSend.add(receiveMsg);
        //app.debugLog(String.format("Message sent from %s to %s\n", receiveMsg.getSrc(), receiveMsg.getReceiverEntity()));

    }

    public void createResultMessage(VCSApp app, Entity source, boolean isDone){
        ResultMsg resultMsg = new ResultMsg(app, source, VCSApp.headQuarter, isDone);
        resultMsg.setCounter(calculateRangeCounter(resultMsg));
        messagesToSend.add(resultMsg);
        app.debugLog(String.format("Message sent from %s to %s\n", resultMsg.getSrc(), resultMsg.getReceiverEntity()));

    }

    public void sendMessage(Message msg){
        Entity receiver;
        if (msg.type == Message.MessageType.ENTITY_INFO){
            for (Entity entity : msg.getReceiverList()){
                receiver = entity;
                receiver.getTdlReceiver().receiveMessage(msg);
            }
        }else {
            receiver = msg.getReceiverEntity();
            receiver.getTdlReceiver().receiveMessage(msg);
        }
        msg.getApp().actionPanel.refreshCurrentOrderPanel();
        //msg.getApp().debugLog(msg.getMsg() + " message received!!!");
    }

    public void update(){

        for(Entity e : source.w.entities){
            if(source.getPos().distance(e.getPos()) <= range && e != source)
                source.addLinkedEntity(e);
        }

        for(Entity e : source.getLinkedEntities()){
            if(source.getPos().distance(e.getPos()) > range)
                source.removeLinkedEntity(e);
        }


        if(!source.getLinkedEntities().isEmpty())
            source.getTdlTransmitter().createInfoMessage(source.w.app , source, source.getLinkedEntities());

        if(!messagesToSend.isEmpty()){
            for(Message msg : messagesToSend){
                if(msg.getCounter() > 0){
                    msg.setCounter(msg.getCounter() - 1);
                    msg.getApp().debugLog(String.format("Message of %s forwarded to relay.", msg.getSrc().getName()));
                }
                else{
                    sendMessage(msg);
                    messagesToRemove.add(msg);
                }
            }
        }
        if(!messagesToRemove.isEmpty()){
            messagesToSend.removeAll(messagesToRemove);
        }


    }

    public int calculateRangeCounter(Message msg){
        int counter  = 0;
        Entity targetReceiver = msg.getReceiverEntity();
        Entity temp = targetReceiver;
        double posDiff;

        while(msg.getSrc().getPos().distance(targetReceiver.getPos()) > msg.getSrc().getTdlTransmitter().getRange()){
            posDiff = msg.getApp().mapView.getWidth();
            for(Entity e : msg.getSrc().getKnownEntities()){
                if(e.getPos().distance(targetReceiver.getPos()) < e.getTdlTransmitter().getRange()){
                    // HAS VISUAL ON TARGET
                    double newDiff = e.getPos().distance(msg.getSrc().getPos());
                    if(newDiff < posDiff){
                        posDiff = newDiff;
                        temp = e;
                    }
                }

            }
            targetReceiver = temp;
            counter++;
            msg.getApp().debugLog(String.format("Relay %d: %s\n", counter, targetReceiver.getName()));
        }
        msg.getApp().debugLog("Connection is done, forwarding...");

        return counter;
    }

    public int getRange(){
        return range;
    }

}
