package Sim.TDL;

import Sim.Component;
import Sim.Entity;
import Sim.Orders.Attack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class TDLReceiverComp extends Component {

    private Queue<Message> receivedMessages = new LinkedList<>();

    public TDLReceiverComp(Entity parent, ArrayList<Entity> entities) {
        super(parent, entities, ComponentType.RECEIVER);
        parent.w.registerReceiver(this);
    }

    public void receiveMessage2(Message msg){
        receivedMessages.add(msg);
    }

    public void readMessage(Message msg){
        if(!msg.getTargetID().equals(parentEntity.getId()) && msg.type != Message.MessageType.ENTITY_INFO){
            return;

        }

        // Or relay, if requested

        switch (msg.type){
            case ATTACK_ORDER:
                parentEntity.addOrder(new Attack(msg.getApp(), parentEntity, msg.getApp().headQuarter, ((AttackMsg) msg).getAttackTargetID()));
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
                if(!msg.getSrcID().equals(parentEntity.getId()))
                    if (parentEntity.getSide().equals(((InfoMsg) msg).getSide()))
                        parentEntity.getLocalWorld().readEntityInfo(msg);

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
                parentEntity.getLocalWorld().readSurveillanceInfo(msg);
                break;

            case KNOWN_INFO:
                if(!msg.getSrcID().equals(parentEntity.getId()))
                    if (parentEntity.getSide().equals(((KnownInfosMsg) msg).getSide())){
                        parentEntity.getLocalWorld().readKnownInfo(msg);
                    }
                break;

        }
    }

    @Override
    public void update(int deltaTime) {
        while(!receivedMessages.isEmpty()){ //TODO: VEYA BİR KOTA KOY, HER UPDATE'DE 20 MESAJ OKU GİBİ
            //source.w.app.debugLog("MSG read");
            // TODO BB global debug nasıl olmalı?
            readMessage(receivedMessages.poll());
        }
    }

}
