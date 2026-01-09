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
        parent.w.registerReceiver(parentEntity.getId(), this);
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
                    ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, msg);
                    break;
                case FOLLOW_ORDER:
                    parentEntity.addOrder(new Follow(msg.getApp(),parentEntity,
                            msg.getApp().getHQ(), ((FollowMsg) msg).getFollowTarget(), ((FollowMsg) msg).getTime()));
                    ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, msg);
                    break;
                case MOVE_ORDER:
                    parentEntity.addOrder(new Move(msg.getApp(), parentEntity,
                            msg.getApp().getHQ(), ((MoveMsg) msg).getPos()));
                    ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, msg);
                    break;
                case ENTITY_INFO:
                    if(!msg.getSrcID().equals(parentEntity.getId()))
                        if (parentEntity.getSide().equals(((InfoMsg) msg).getSide()))
                            parentEntity.getLocalWorld().readEntityInfo(msg);
                    if(parentEntity.getComponent(Component.ComponentType.TRANSMITTER) == null)
                        return;
                    ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, msg);
                    break;
                case RECEIVE_INFO:
                   // msg.getApp().debugLog("Message arrived successfully.");
                    break;
                case ORDER_RESULT:
                    ResultMsg rm = (ResultMsg) msg;
                    if (rm.getOrderResult() == 0){
                        msg.getApp().debugLog("Order done!");
                        //order tamamlanmış okey
                    } else{
                        msg.getApp().debugLog("Order not done!");

                    }
                    break;
                case SURVEILLANCE_MSG:

                    parentEntity.getLocalWorld().readSurveillanceInfo(msg);
                    ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, msg);
                    break;
                case KNOWN_INFO:
                    if(!msg.getSrcID().equals(parentEntity.getId()))
                        if (parentEntity.getSide().equals(((KnownInfosMsg) msg).getSide())){
                            parentEntity.getLocalWorld().readKnownInfo(msg);
                            if(parentEntity.getComponent(Component.ComponentType.TRANSMITTER) == null)
                                return;
                            ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, msg);

                        }
                    break;
                case MISSION_ABORT:
                    ((TDLTransmitterComp) parentEntity.getComponent(Component.ComponentType.TRANSMITTER)).createReceiveMessage2(msg.getApp(), parentEntity, msg);
            }
        }
    }

    @Override
    public void update(int deltaTime) {
        while(!receivedMessages.isEmpty()){
            //source.w.app.debugLog("MSG read");
            readMessage(receivedMessages.poll());
        }
    }

    @Override
    public TDLReceiverComp copyTo(Entity e) {
        return new TDLReceiverComp(e, entities);
    }

}
