package Sim.TDL;

import App.VCSApp;
import Sim.Component;
import Sim.Entity;
import Vec.Vec2int;

import java.util.ArrayList;

public class TDLTransmitterComp extends Component {

    private int accSelfInfo = 0;
    private final int ACC_SELF_INFO_TIME = 2000;
    private int accAllInfo = 0;
    private final int ACC_ALL_INFO_TIME = 4000;


    public TDLTransmitterComp(Entity parent, ArrayList<Entity> entities) {
        super(parent, entities);
        //parent.w.registerTransmitter(this);
    }

    public void send(Message msg) {
        parentEntity.w.send(msg);
    }
    @Override
    public void update(int deltaTime) {
        accAllInfo += deltaTime;
        accSelfInfo += deltaTime;

        if(accAllInfo >= ACC_ALL_INFO_TIME){
            createKnownInfoMessage(parentEntity.w.app, parentEntity, parentEntity.getLocalWorld().getEntities());
            //createInfoMessage(source.w.app, source);
            accAllInfo -= ACC_ALL_INFO_TIME;
            accSelfInfo -= ACC_SELF_INFO_TIME;
        }
        else if(accSelfInfo >= ACC_SELF_INFO_TIME){
            createInfoMessage(parentEntity.w.app, parentEntity);
            accSelfInfo -= ACC_SELF_INFO_TIME;
        }
    }

    public void createMoveMessage(VCSApp app, Entity targetReceiver, Vec2int pos){
        MoveMsg moveMsg = new MoveMsg(app, VCSApp.headQuarter, targetReceiver, pos);
        //moveMsg.setCounter(calculateRangeCounter(moveMsg));
//        messagesToSend.add(moveMsg);
        send(moveMsg);
        app.debugLog(String.format("Message sent from %s to %s", moveMsg.getSrcID(), moveMsg.getTargetID()));
    }

    public void createAttackMessage(VCSApp app, String targetID, String attackTargetID){
        AttackMsg attackMsg = new AttackMsg(app, VCSApp.headQuarter.getId(), targetID, attackTargetID);
        //attackMsg.setCounter(calculateRangeCounter(attackMsg));
        //messagesToSend.add(attackMsg);
        send(attackMsg);
        app.debugLog(String.format("Message sent from %s to %s", attackMsg.getSrcID(), attackMsg.getTargetID()));
    }

    public void createFollowMessage(VCSApp app, Entity targetReceiver, Entity followEntity, int time){
        FollowMsg followMsg = new FollowMsg(app, VCSApp.headQuarter, targetReceiver, followEntity, time);
        //followMsg.setCounter(calculateRangeCounter(followMsg));
        //messagesToSend.add(followMsg);
        send(followMsg);
        app.debugLog(String.format("Message sent from %s to %s\n", followMsg.getSrcID(), followMsg.getTargetID()));
    }

    public void createInfoMessage(VCSApp app, Entity src){
        //TODO nasıl createlenip ne şekilde ne zaman basılcağına bakmalı
        String targetID = " ";
        InfoMsg infoMsg = new InfoMsg(app, src.getId(), targetID, src.getName(), src.getSide(), src.getPos(), src.getSpeed(), src.getType());
        //messagesToSend.add(infoMsg);
        send(infoMsg);
    }

    public void createKnownInfoMessage(VCSApp app, Entity src, ArrayList<Entity> knownEntities){
        String targetID = " ";
        ArrayList<Entity> selfIncludedList = (ArrayList<Entity>) knownEntities.clone();
        selfIncludedList.addFirst(parentEntity);
        KnownInfosMsg knownInfoMsg = new KnownInfosMsg(app, src.getSide(), src.getId(), targetID, src.getName(), selfIncludedList);
        //messagesToSend.add(infoMsg);
        send(knownInfoMsg);
    }

    public void createReceiveMessage(VCSApp app, Entity source, Message.MessageType type){
        ReceiveMsg receiveMsg = new ReceiveMsg(app, source, VCSApp.headQuarter, type);
        //receiveMsg.setCounter(calculateRangeCounter(receiveMsg));
        //messagesToSend.add(receiveMsg);
        send(receiveMsg);
        //app.debugLog(String.format("Message sent from %s to %s\n", receiveMsg.getSrc(), receiveMsg.getReceiverEntity()));

    }

    public void createResultMessage(VCSApp app, Entity source, boolean isDone){
        ResultMsg resultMsg = new ResultMsg(app, source, VCSApp.headQuarter, isDone);
        //resultMsg.setCounter(calculateRangeCounter(resultMsg));
        //messagesToSend.add(resultMsg);
        send(resultMsg);
        app.debugLog(String.format("Message sent from %s to %s\n", resultMsg.getSrcID(), resultMsg.getTargetID()));

    }

    public void createSurveillanceMsg(VCSApp app, Entity source, String targetID, Entity seenEntity){
        SurveillanceMsg surveillanceMsg = new SurveillanceMsg(app, source, targetID, seenEntity);
        //messagesToSend.add(surveillanceMsg);
        send(surveillanceMsg);
    }

    public void createMissionStartMessage(VCSApp app, String srcID, String missionType){
        MissionStartMsg missionStartMsg = new MissionStartMsg(app, srcID, app.headQuarter.getId(), missionType);
        send(missionStartMsg);
    }
}
