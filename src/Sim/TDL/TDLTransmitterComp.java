package Sim.TDL;

import App.VCSApp;
import Sim.Component;
import Sim.Entity;
import Sim.Orders.Order;
import Vec.Vec2int;

import java.util.ArrayList;
import java.util.HashMap;

public class TDLTransmitterComp extends Component {

    private int accSelfInfo = 0;
    private final int ACC_SELF_INFO_TIME = 2000;
    private int accAllInfo = 0;
    private final int ACC_ALL_INFO_TIME = 4000;
    private int range = 300;
    private boolean isRelayCreated = false;
    private HashMap<Integer, RelayMsg> relayMessages = new HashMap<>();


    public TDLTransmitterComp(Entity parent, ArrayList<Entity> entities) {
        super(parent, entities, ComponentType.TRANSMITTER);
        //parent.w.registerTransmitter(this);
    }

    public void send(Message msg) {
        parentEntity.w.send(msg);
//        parentEntity.w.app.logPanel.toLog(msg);
    }

    public void createMoveMessage2(VCSApp app, Entity targetReceiver, Vec2int pos){
        MoveMsg moveMsg = new MoveMsg(app, app.getHQ(), targetReceiver, pos);
        //moveMsg.setCounter(calculateRangeCounter(moveMsg));
//        messagesToSend.add(moveMsg);
        send(moveMsg);
        app.debugLog(String.format("Message sent from %s to %s", moveMsg.getSrcID(), moveMsg.getTargetID()));
    }

    public void createAttackMessage2(VCSApp app, String targetID, String attackTargetID){
        AttackMsg attackMsg = new AttackMsg(app, app.getHQ().getId(), targetID, attackTargetID);
        //attackMsg.setCounter(calculateRangeCounter(attackMsg));
        //messagesToSend.add(attackMsg);
        send(attackMsg);
        app.debugLog(String.format("Message sent from %s to %s", attackMsg.getSrcID(), attackMsg.getTargetID()));
    }

    public void createFollowMessage2(VCSApp app, Entity targetReceiver, Entity followEntity, int time){
        FollowMsg followMsg = new FollowMsg(app, app.getHQ(), targetReceiver, followEntity, time);
        //followMsg.setCounter(calculateRangeCounter(followMsg));
        //messagesToSend.add(followMsg);
        send(followMsg);
        app.debugLog(String.format("Message sent from %s to %s\n", followMsg.getSrcID(), followMsg.getTargetID()));
    }

    public void createInfoMessage2(VCSApp app, Entity src){
        //TODO nasıl createlenip ne şekilde ne zaman basılcağına bakmalı
        String targetID = " ";
        InfoMsg infoMsg = new InfoMsg(app, src.getId(), targetID, src.getName(), src.getSide(), src.getPos(), src.getSpeed(), src.getType());
        //messagesToSend.add(infoMsg);
        send(infoMsg);
    }

    public void createKnownInfoMessage2(VCSApp app, Entity src, ArrayList<Entity> knownEntities){
        String targetID = " ";
        ArrayList<Entity> selfIncludedList = (ArrayList<Entity>) knownEntities.clone();
        selfIncludedList.addFirst(parentEntity);
        KnownInfosMsg knownInfoMsg = new KnownInfosMsg(app, src.getSide(), src.getId(), targetID, selfIncludedList);
        //messagesToSend.add(infoMsg);
        send(knownInfoMsg);
    }

    public void createReceiveMessage2(VCSApp app, Entity source, Message.MessageType type){
        ReceiveMsg receiveMsg = new ReceiveMsg(app, source, app.getHQ(), type);
        //receiveMsg.setCounter(calculateRangeCounter(receiveMsg));
        //messagesToSend.add(receiveMsg);
        send(receiveMsg);
        //app.debugLog(String.format("Message sent from %s to %s\n", receiveMsg.getSrc(), receiveMsg.getReceiverEntity()));

    }

    public void createResultMessage2(VCSApp app, Entity source, int finishStat, Order.OrderType type){
        ResultMsg resultMsg = new ResultMsg(app, source, app.getHQ(), finishStat, type);
        //resultMsg.setCounter(calculateRangeCounter(resultMsg));
        //messagesToSend.add(resultMsg);
        send(resultMsg);
    }

    public void createSurveillanceMsg2(VCSApp app, Entity source, Entity hostileEntity){
        String targetID = " ";
        SurveillanceMsg surveillanceMsg = new SurveillanceMsg(app, source, targetID, hostileEntity);
        //messagesToSend.add(surveillanceMsg);
        send(surveillanceMsg);
    }

    public void createMissionStartMessage2(VCSApp app, String srcID, String missionType){
        MissionStartMsg missionStartMsg = new MissionStartMsg(app, srcID, app.getHQ().getId(), missionType);
        send(missionStartMsg);
    }

    public void createRelayMessage(VCSApp app, String relaySrcID, Message msg){
        isRelayCreated = true;
        RelayMsg relayMsg = new RelayMsg(app, relaySrcID, msg.getTargetID(), msg);
        relayMessages.put(relayMsg.getMsgID(), relayMsg);
    }

    public void createMissionAbortMessage(VCSApp app, String srcID, String targetID, Order.OrderType type){
        MissionAbortMsg abortMsg = new MissionAbortMsg(app, srcID, targetID, type);
        send(abortMsg);
    }

    @Override
    public void update(int deltaTime) {
        accAllInfo += deltaTime;
        accSelfInfo += deltaTime;

        if(accAllInfo >= ACC_ALL_INFO_TIME){
            createKnownInfoMessage2(parentEntity.w.app, parentEntity, parentEntity.getLocalWorld().getEntities());
            //createInfoMessage(source.w.app, source);
            accAllInfo -= ACC_ALL_INFO_TIME;
            accSelfInfo -= ACC_SELF_INFO_TIME;
        }
        else if(accSelfInfo >= ACC_SELF_INFO_TIME){
            createInfoMessage2(parentEntity.w.app, parentEntity);
            accSelfInfo -= ACC_SELF_INFO_TIME;
        }

        if (isRelayCreated){
            for (int id : relayMessages.keySet()){
                send(relayMessages.remove(id));
            }
            isRelayCreated = false;
        }
    }

    @Override
    public TDLTransmitterComp copyTo(Entity e) {
        return new TDLTransmitterComp(e, entities);
    }

    public int getTransmitterRange(){
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
