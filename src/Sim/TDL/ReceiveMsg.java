package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import UI.UIColorManager;

import java.awt.*;

public class ReceiveMsg extends Message{
    private final MessageType msgType;

    public ReceiveMsg(VCSApp app, Entity src, String receiverID, Message msg) {
        super(MessageType.RECEIVE_INFO, app, src.getId(), receiverID, src.getId(), "J0.1");
        this.msgType = msg.type;
    }

    public String receiveDetail(){
        String detailMsg;
        switch (msgType){
            case ATTACK_ORDER:
                detailMsg = String.format("Acknowledgment Message:\nFrom: %s\nTo: %s\nReceived Message Type: Attack Order\n", getSrcID(), getTargetID());
                break;
            case MOVE_ORDER:
                detailMsg = String.format("Acknowledgment Message:\nFrom: %s\nTo: %s\nReceived Message Type: Move Order\n", getSrcID(), getTargetID());
                break;
            case FOLLOW_ORDER:
                detailMsg = String.format("Acknowledgment Message:\nFrom: %s\nTo: %s\nReceived Message Type: Follow Order\n", getSrcID(), getTargetID());
                break;
            case MISSION_ABORT:
                detailMsg = String.format("Acknowledgment Message:\nFrom: %s\nTo: %s\nReceived Message Type: Mission Abort\n", getSrcID(), getTargetID());
                break;
            case ENTITY_INFO:
                detailMsg = String.format("Acknowledgment Message:\nFrom: %s\nTo: %s\nReceived Message Type: Entity Info", getSrcID(), getTargetID());
                break;
            case KNOWN_INFO:
                detailMsg = String.format("Acknowledgment Message:\nFrom: %s\nTo: %s\nReceived Message Type: Info of Known", getSrcID(), getTargetID());
                break;
            case SURVEILLANCE_MSG:
                detailMsg = String.format("Acknowledgment Message:\nFrom: %s\nTo: %s\nReceived Message Type: Surveillance", getSrcID(), getTargetID());
                break;
            case RELAY:
                detailMsg = String.format("Acknowledgment Message:\nFrom: %s\nTo: %s\nReceived Message Type: Relay", getSrcID(), getTargetID());
                break;
            default:
                detailMsg = String.format("Acknowledgment Message:\nFrom: %s\nTo: %s\nReceived Message Type: Invalid", getSrcID(), getTargetID());
        }
        return detailMsg;
    }

    public MessageType getMsgType(){
        return msgType;
    }

    @Override
    public String getMsgDetail() {
        return receiveDetail();
    }

    @Override
    public Color getColor() {
        return UIColorManager.J0_COLOR;
    }

    @Override
    public Message copy() {
        return null;
    }
}
