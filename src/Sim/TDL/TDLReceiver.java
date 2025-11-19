package Sim.TDL;

import Sim.Entity;
import Sim.Orders.Attack;
import Sim.Orders.Follow;
import Sim.Orders.Move;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class TDLReceiver {

    private Entity source;
    private Queue<Message> receivedMessages = new LinkedList<>();

    public TDLReceiver(Entity source){
        this.source = source;
    }

    public void receiveMessage2(Message msg){
        receivedMessages.add(msg);
    }

    public void update(){
        while(!receivedMessages.isEmpty()){ //TODO: VEYA BİR KOTA KOY, HER UPDATE'DE 20 MESAJ OKU GİBİ
            //source.w.app.debugLog("MSG read");
            // TODO BB global debug nasıl olmalı?
            readMessage(receivedMessages.poll());
        }
    }

    public void readMessage(Message msg){
        //source.w.app.debugLogError("Message reading with type: " + msg.type.toString());
        if(!msg.getTargetID().equals(source.getId()) && msg.type != Message.MessageType.ENTITY_INFO){
            return;

        }

            // Or relay, if requested

        switch (msg.type){
            case ATTACK_ORDER:
                source.addOrder(new Attack(msg.getApp(), source, msg.getApp().headQuarter, ((AttackMsg) msg).getAttackTargetID()));
                System.out.println("read the attack message");
                break;
            case FOLLOW_ORDER:
//                source.addOrder(new Follow(msg.getApp(), msg.getTargetReceiver(),
//                        ((FollowMsg) msg).getFollowTarget(), ((FollowMsg) msg).getTime()));
                break;
            case MOVE_ORDER:
//                source.addOrder(new Move(msg.getApp(), msg.getTargetReceiver(),
//                        ((MoveMsg) msg).getPos()));
                break;
            case ENTITY_INFO:
                if(!msg.getSrcID().equals(source.getId()))
                    if (source.getSide().equals(((InfoMsg) msg).getSide()))
                        source.getLocalWorld().readEntityInfo(msg);

                break;
            case RECEIVE_INFO:
                msg.getApp().debugLog("Message arrived successfully.");
                //TODO orderının receivelendiğini öğreninice nolcak bilmiyorum
                break;
            case ORDER_RESULT:
                ResultMsg rm = (ResultMsg) msg;
                if (rm.getOrderResult()){
                    msg.getApp().debugLog("Order done!"); // TODO BB which order ?
                    //order tamamlanmış okey
                } else {
                    msg.getApp().debugLog("Order not done!");
                    //TODO order tamalanamamış, nedeni ve ne orderı olduğuna bakılıp tekrar emir verilinebilir
                }
                break;
            case SURVEILLANCE_MSG:
                //TODO with local create func create entity and add to knownentities of target
                source.getLocalWorld().readSurveillanceInfo(msg);
                break;

            case KNOWN_INFO:
                if(!msg.getSrcID().equals(source.getId()))
                    if (source.getSide().equals(((KnownInfosMsg) msg).getSide())){
                        source.getLocalWorld().readKnownInfo(msg);
                    }
                break;

        }
    }

//    public void receiveMessage(Message msg){
//        if(msg.type == Message.MessageType.ATTACK_ORDER){
//            source.addOrder(new Attack(msg.getApp(), msg.getTargetReceiver(),
//                    ((AttackMsg) msg).getTarget()));
//        }
//        else if(msg.type == Message.MessageType.FOLLOW_ORDER){
//            source.addOrder(new Follow(msg.getApp(), msg.getTargetReceiver(),
//                    ((FollowMsg) msg).getFollowTarget(), ((FollowMsg) msg).getTime()));
//
//        }
//        else if(msg.type == Message.MessageType.MOVE_ORDER){
//            source.addOrder(new Move(msg.getApp(), msg.getTargetReceiver(),
//                    ((MoveMsg) msg).getPos()));
//        }
//        else if(msg.type == Message.MessageType.ENTITY_INFO){
//            //source.addLinkedEntity(msg.getSrc());
//            for (Entity e : msg.getSrc().getLinkedEntities()){
//                if (!source.getLinkedEntities().contains(e) && e != source){
//                    //source.addLinkedEntity(e);
//                }
//            }
//
//        }
//        else if (msg.type == Message.MessageType.RECEIVE_INFO) {
//            msg.getApp().debugLog("Message arrived successfully.");
//            //TODO orderının receivelendiğini öğreninice nolcak bilmiyorum
//        }
//        else if (msg.type == Message.MessageType.ORDER_RESULT) {
//            ResultMsg rm = (ResultMsg) msg;
//            if (rm.getOrderResult()){
//                msg.getApp().debugLog("Order done!");
//                //order tamamlanmış okey
//            } else {
//                msg.getApp().debugLog("Order not done!");
//                //TODO order tamalanamamış, nedeni ve ne orderı olduğuna bakılıp tekrar emir verilinebilir
//            }
//        }
//    }


}
