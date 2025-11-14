package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

import java.util.ArrayList;
import java.util.List;

public class TDLTransmitter {

    private Entity source;
    private int range = 500;

//    private ArrayList<Message> messagesToSend = new ArrayList<>();
//    private ArrayList<Message> messagesToRemove = new ArrayList<>();
    public TDLTransmitter(Entity source){
        this.source = source;
    }

    public void createMoveMessage(VCSApp app, Entity targetReceiver, Vec2int pos){
       MoveMsg moveMsg = new MoveMsg(app, VCSApp.headQuarter, targetReceiver, pos);
        //moveMsg.setCounter(calculateRangeCounter(moveMsg));
//        messagesToSend.add(moveMsg);
        sendMessage2(moveMsg);
        app.debugLog(String.format("Message sent from %s to %s", moveMsg.getSrcID(), moveMsg.getTargetID()));
    }

    public void createAttackMessage(VCSApp app, String targetID, String attackTargetID){
        AttackMsg attackMsg = new AttackMsg(app, VCSApp.headQuarter.getId(), targetID, attackTargetID);
        //attackMsg.setCounter(calculateRangeCounter(attackMsg));
        //messagesToSend.add(attackMsg);
        sendMessage2(attackMsg);
        app.debugLog(String.format("Message sent from %s to %s", attackMsg.getSrcID(), attackMsg.getTargetID()));
    }

    public void createFollowMessage(VCSApp app, Entity targetReceiver, Entity followEntity, int time){
        FollowMsg followMsg = new FollowMsg(app, VCSApp.headQuarter, targetReceiver, followEntity, time);
        //followMsg.setCounter(calculateRangeCounter(followMsg));
        //messagesToSend.add(followMsg);
        sendMessage2(followMsg);
        app.debugLog(String.format("Message sent from %s to %s\n", followMsg.getSrcID(), followMsg.getTargetID()));
    }

    public void createInfoMessage(VCSApp app, Entity src){
        //TODO nasıl createlenip ne şekilde ne zaman basılcağına bakmalı
        String targetID = " ";
        InfoMsg infoMsg = new InfoMsg(app, src.getId(), targetID, src.getName(), src.getSide(), src.getPos(), src.getSpeed(), src.getType());
        //messagesToSend.add(infoMsg);
        sendMessage2(infoMsg);
    }

    public void createReceiveMessage(VCSApp app, Entity source, Message.MessageType type){
        ReceiveMsg receiveMsg = new ReceiveMsg(app, source, VCSApp.headQuarter, type);
        //receiveMsg.setCounter(calculateRangeCounter(receiveMsg));
        //messagesToSend.add(receiveMsg);
        sendMessage2(receiveMsg);
        //app.debugLog(String.format("Message sent from %s to %s\n", receiveMsg.getSrc(), receiveMsg.getReceiverEntity()));

    }

    public void createResultMessage(VCSApp app, Entity source, boolean isDone){
        ResultMsg resultMsg = new ResultMsg(app, source, VCSApp.headQuarter, isDone);
        //resultMsg.setCounter(calculateRangeCounter(resultMsg));
        //messagesToSend.add(resultMsg);
        sendMessage2(resultMsg);
        app.debugLog(String.format("Message sent from %s to %s\n", resultMsg.getSrcID(), resultMsg.getTargetID()));

    }

    public void createSurveillanceMsg(VCSApp app, Entity source, String targetID, Entity seenEntity){
        SurveillanceMsg surveillanceMsg = new SurveillanceMsg(app, source, targetID, seenEntity);
        //messagesToSend.add(surveillanceMsg);
        sendMessage2(surveillanceMsg);
    }

    public void  sendMessage2(Message msg){
        //TODO: range içindeki herkesin mesaj listesine mesajı gönder,
        // receive classında ise her update içinde en üstteki mesajı oku
        if (!source.isLocal()){
            if (!msg.getTargetID().equals(" ")){
                String targetID = msg.getTargetID();
                for(Entity e : source.w.entityHashMap.values()){
//                   if(e == source);
//                      //continue; //BURASI EMIR ALMA MESJLARINI BOZUYOR
                    if (e.getId().equals(targetID) && (source.getPos().distance(e.getPos()) < range) && !e.isLocal()) {
                        msg.getApp().logPanel.toLog(msg);
                        e.getTdlReceiver().receiveMessage2(msg);
                    }
                }
            } else if (msg.type.equals(Message.MessageType.ENTITY_INFO)){
                for (Entity e : source.w.entityHashMap.values()){
                    if ((source.getPos().distance(e.getPos()) < range) && !e.isLocal() && !e.equals(source)){
                        if (!source.getId().equals(e.getId()) && ((source.getId().equals("HQ") || source.getId().charAt(0) == 'A') && (e.getId().equals("HQ") || e.getId().charAt(0) == 'A'))) {
                            msg.setTargetID(e.getId());
                        } else continue;
                        msg.getApp().logPanel.toLog(msg);
                        e.getTdlReceiver().receiveMessage2(msg);
                    }
                }
            }
        }





//        if(msg.type != Message.MessageType.ENTITY_INFO &&
//        source.w.entityHashMap.get(msg.getTargetID()).getPos().distance(source.getPos()) > range){
//           relayMessage(msg);
//        } //todo: info bilgisinin relayini hallet önce. infoda target olmadığı için hashmapten target range
        //todo:      içinde mi kontrol edemiyi kotalarda, bildiği bütün kişilerin infosunu bağırsın
        //todo f1 cont. : böylelikle herkes, bildiği kişilerin bildiklerini de öğrenmiş olur ama bu mantıklı mı bilmiyorum
        //todo: bilmeyen kişinin bana bunun infosunu ver demesi saçma çünkü bilmiyor,
        // her entity için bildiklerim bildiklerimi biliyor mu kontrolü yapıp eksik varsa gerekli infoyu iletebilir



    }


//    public void sendMessage(Message msg){
//        Entity receiver;
//        if (msg.type == Message.MessageType.ENTITY_INFO){
//            for (Entity entity : msg.getReceiverList()){
//                receiver = entity;
//                receiver.getTdlReceiver().receiveMessage(msg);
//            }
//        }else {
//            receiver = msg.getTargetReceiver();
//            receiver.getTdlReceiver().receiveMessage(msg);
//        }
//        msg.getApp().actionPanel.refreshCurrentOrderPanel();
//        //msg.getApp().debugLog(msg.getMsg() + " message received!!!");
//    }

    public void update(){

        createInfoMessage(source.w.app, source);

//        for(Entity e : source.w.entities){
//            if(source.getPos().distance(e.getPos()) <= range && e != source)
//                source.addLinkedEntity(e);
//        }
//
//        for(Entity e : source.getLinkedEntities()){
//            if(source.getPos().distance(e.getPos()) > range)
//                source.removeLinkedEntity(e);
//        }
//
//
//        if(!source.getLinkedEntities().isEmpty())
//            for (Entity entity : source.getLinkedEntities()){
//                String id = entity.getId();
//                source.getTdlTransmitter().createInfoMessage(source.w.app , source, id);
//            }
//
//        if(!messagesToSend.isEmpty()){
//            for(Message msg : messagesToSend){
//                if(msg.getCounter() > 0){
//                    msg.setCounter(msg.getCounter() - 1);
//                    msg.getApp().debugLog(String.format("Message of %s forwarded to relay.", msg.getSrcID()));
//                }
//                else{
//                    sendMessage2(msg);
//                    messagesToRemove.add(msg);
//                }
//            }
//        }
//        if(!messagesToRemove.isEmpty()){
//            messagesToSend.removeAll(messagesToRemove);
//        }


    }

//    public int calculateRangeCounter(Message msg){ TODO: silme burayı, shortest path için lazım olacak
//        int counter  = 0;
//        Entity targetReceiver = msg.getTargetReceiver();
//        Entity temp = targetReceiver;
//        Entity source = msg.getSrc();
//        VCSApp app = msg.getApp();
//        double posDiff;
//
//        if(source.getLinkedEntities().contains(targetReceiver)){
//            while(source.getPos().distance(targetReceiver.getPos()) >
//                    source.getTdlTransmitter().getTransmitterRange()){
//                posDiff = app.mapView.getWidth();
//                for(Entity e : source.getLinkedEntities()){
//                    if(e.getPos().distance(targetReceiver.getPos()) < e.getTdlTransmitter().getTransmitterRange()){
//                        // HAS VISUAL ON TARGET
//                        double newDiff = e.getPos().distance(source.getPos());
//                        if(newDiff < posDiff){
//                            posDiff = newDiff;
//                            temp = e;
//                        }
//                    }
//                }
//                targetReceiver = temp;
//                counter++;
//                app.debugLog(String.format("Relay %d: %s\n", counter, targetReceiver.getName()));
//            }
//        }
//        app.debugLog("Connection is done, forwarding...");
//
//        return counter;
//    }


        public void relayMessage(Message msg){ //TODO: silme burayı, shortest path için lazım olacak
        //int counter  = 0; //todo, info mesaj ulaşmadıysa hedefli info oluştur ve yolla, diğerleri için sadece relay
        Entity targetReceiver = source.w.entityHashMap.get(msg.getTargetID());
        Entity relay = targetReceiver;
        Entity src = source.w.entityHashMap.get(msg.getSrcID());
        VCSApp app = msg.getApp();
        double posDiff;

        if(src.getLocalWorld().getEntities().contains(targetReceiver)){ // gönderici, alıcıyı biliyorsa
            posDiff = app.mapView.getWidth();
            for(Entity e : src.getLocalWorld().getEntities()){
                if(e.getPos().distance(targetReceiver.getPos()) < e.getTdlTransmitter().getTransmitterRange()){
                    // HAS VISUAL ON TARGET
                    double newDiff = e.getPos().distance(src.getPos());
                    if(newDiff < posDiff){
                        posDiff = newDiff;
                        relay = e;
                    }
                }
                targetReceiver = relay; // relay for next forwarding
//                counter++;
//                app.debugLog(String.format("Relay %d: %s\n", counter, targetReceiver.getName()));
            }
        }
        app.debugLog("Connection is done, forwarding...");

        if(targetReceiver.getPos().distance(source.getPos()) <= range){
            targetReceiver.getTdlReceiver().receiveMessage2(msg);
            source.w.app.debugLog("MESSAGE RECEIVED FROM THE RELAY...");
        }
        else{
            targetReceiver.getTdlTransmitter().relayMessage(msg);
        }
        //TODO: BURALARI DAHA RUNLAYAMADIN PC BOZULDUGU İCİN. TEST ET!!!!!!!
    }



    public int getTransmitterRange(){
        return range;
    }

}
