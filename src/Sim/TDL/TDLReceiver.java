package Sim.TDL;

import Sim.Entity;
import Sim.Orders.Attack;
import Sim.Orders.Follow;
import Sim.Orders.Move;

import java.util.ArrayList;
import java.util.Objects;

public class TDLReceiver {

    private Entity source;
    private ArrayList<Message> receivedMessages;

    public TDLReceiver(Entity source){
        this.source = source;
    }

    public void receiveMessage2(Message msg){
        receivedMessages.add(msg);
        readMessage(msg);
    }

    public void readMessage(Message msg){
        if(msg.getTargetID().equals(source.getId()))
            return;
        // Or relay, if requested


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
