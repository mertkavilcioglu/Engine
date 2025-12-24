package Sim.TDL;

import Sim.Component;
import Sim.Entity;
import Sim.Orders.Attack;
import Sim.Orders.Follow;
import Sim.Orders.Move;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class TDLReceiverComp extends Component {

    private Queue<Message> receivedMessages = new LinkedList<>();

    public TDLReceiverComp(Entity parent, ArrayList<Entity> entities) {
        super(parent, entities, ComponentType.RECEIVER);
        parent.w.registerReceiver(this, parentEntity);
    }

    public void receiveMessage2(Message msg){
        receivedMessages.add(msg);
    }

    public void readMessage(Message msg){
        if(!msg.getTargetID().equals(parentEntity.getId()) && !msg.type.equals(Message.MessageType.RELAY)){
            return;
        } else if (!msg.getTargetID().equals(parentEntity.getId()) && msg.type.equals(Message.MessageType.RELAY)) {
            ((TDLTransmitterComp) parentEntity.getComponent(ComponentType.TRANSMITTER)).createRelayMessage(msg.getApp(), parentEntity.getId(), ((RelayMsg) msg).getRealMsg());
        } else {
            if (msg.type.equals(Message.MessageType.RELAY)){
                msg = ((RelayMsg) msg).getRealMsg();
            }
            switch (msg.type){
                case ATTACK_ORDER:
                    parentEntity.addOrder(new Attack(msg.getApp(), parentEntity,
                            msg.getApp().getHQ(), ((AttackMsg) msg).getAttackTargetID()));
                    System.out.println("read the attack message");
                    ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, Message.MessageType.ATTACK_ORDER);
                    break;
                case FOLLOW_ORDER:
                    parentEntity.addOrder(new Follow(msg.getApp(),parentEntity,
                            msg.getApp().getHQ(), ((FollowMsg) msg).getFollowTarget(), ((FollowMsg) msg).getTime()));
                    ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, Message.MessageType.FOLLOW_ORDER);
                    break;
                case MOVE_ORDER:
                    parentEntity.addOrder(new Move(msg.getApp(), parentEntity,
                            msg.getApp().getHQ(), ((MoveMsg) msg).getPos()));
                    ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, Message.MessageType.MOVE_ORDER);
                    break;
                case ENTITY_INFO:
                    if(!msg.getSrcID().equals(parentEntity.getId()))
                        if (parentEntity.getSide().equals(((InfoMsg) msg).getSide()))
                            parentEntity.getLocalWorld().readEntityInfo(msg);
                    ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, Message.MessageType.ENTITY_INFO);
                    break;
                case RECEIVE_INFO:
                   // msg.getApp().debugLog("Message arrived successfully.");
                    //TODO orderının receivelendiğini öğreninice nolcak bilmiyorum
                    break;
                case ORDER_RESULT:
                    ResultMsg rm = (ResultMsg) msg;
                    if (rm.getOrderResult() == 0){
                        msg.getApp().debugLog("Order done!"); // TODO BB which order ?
                        //order tamamlanmış okey
                    } else{
                        msg.getApp().debugLog("Order not done!");
                        //TODO order tamalanamamış, nedeni ve ne orderı olduğuna bakılıp tekrar emir verilinebilir
                    }
                    break;
                case SURVEILLANCE_MSG:
                    //TODO with local create func create entity and add to knownentities of target
                    parentEntity.getLocalWorld().readSurveillanceInfo(msg);
                    ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, Message.MessageType.SURVEILLANCE_MSG);
                    break;
                case KNOWN_INFO:
                    if(!msg.getSrcID().equals(parentEntity.getId()))
                        if (parentEntity.getSide().equals(((KnownInfosMsg) msg).getSide())){
                            parentEntity.getLocalWorld().readKnownInfo(msg);
                            ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, Message.MessageType.KNOWN_INFO);

                        }
                    break;
                case MISSION_ABORT:
                    ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, Message.MessageType.MISSION_ABORT);
            }
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

    @Override
    public TDLReceiverComp copyTo(Entity e) {
        return new TDLReceiverComp(e, entities);
    }

}
