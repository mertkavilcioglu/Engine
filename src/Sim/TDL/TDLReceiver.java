package Sim.TDL;

import Sim.Entity;

import java.util.ArrayList;

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
        if(msg.getTargetID().equals(source.getId())) {
            return;
            // Or relay, if requested
        } else {
            if (msg.type.equals(Message.MessageType.ENTITY_INFO)){
                //TODO create local world and write a create func for that world
                // than with that func create a entity and add to linkedEntities
            } else if (msg.type.equals(Message.MessageType.SURVEILLANCE_MSG)) {
                //TODO with local create func create entity and add to knownentities of target
            } else if (msg.type.equals(Message.MessageType.RECEIVE_INFO)) {

            } else if (msg.type.equals(Message.MessageType.ATTACK_ORDER)) {

            } else if (msg.type.equals(Message.MessageType.MOVE_ORDER)) {

            }else if (msg.type.equals(Message.MessageType.FOLLOW_ORDER)){

            }else if (msg.type.equals(Message.MessageType.ORDER_RESULT)){

            }
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
