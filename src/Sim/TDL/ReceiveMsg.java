package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

import java.awt.*;

public class ReceiveMsg extends Message{
//TODO içini doldur
    private MessageType order;

    public ReceiveMsg(VCSApp app, Entity src, Entity receiver, MessageType orderType) {
        super(MessageType.RECEIVE_INFO, app, src.getId(), receiver.getId(), src.getId(), "J1.0");
        //super.setMsg("J1.0");
        this.order = orderType;
    }

    public String receiveDetail(){
        if (order == MessageType.ATTACK_ORDER){
            return String.format("Receive The Order Message:\nFrom: %s\nTo: %s\nOrder Type: Attack\n%s receive the order from %s.", getSrcID(), getTargetID(), getSrcID(), getTargetID());
        } else if (order == MessageType.MOVE_ORDER){
            return String.format("Receive The Order Message:\nFrom: %s\nTo: %s\nOrder Type: Move\n%s receive the order from %s.", getSrcID(), getTargetID(), getSrcID(), getTargetID());
        }else if (order == MessageType.FOLLOW_ORDER){
            return String.format("Receive The Order Message:\nFrom: %s\nTo: %s\nOrder Type: Follow\n%s receive the order from %s.", getSrcID(), getTargetID(), getSrcID(), getTargetID());
        }else return null;
    }

    @Override
    public String getMsgDetail() {
        return receiveDetail();
    }

    @Override
    public Color getColor() {
        return getApp().uiColorManager.J0_COLOR;
    }
}
