package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

public class ReceiveMsg extends Message{
//TODO içini doldur
    private MessageType order;

    public ReceiveMsg(VCSApp app, Entity src, Entity receiver, MessageType orderType) {
        super(MessageType.RECEIVE_INFO, app, src, receiver, (String.format("%s: %s",src, "J1.0")));
        this.order = orderType;
    }

    public String receiveDetail(){
        if (order == MessageType.ATTACK_ORDER){
            return String.format("Receive The Order Message:\nFrom: %s\nTo: %s\nOrder Type: Attack\n%s going to execute the order from %s.", getSrc().getName(), getReceiverEntity().getName(), getSrc().getName(), getReceiverEntity().getName());
        } else if (order == MessageType.MOVE_ORDER){
            return String.format("Receive The Order Message:\nFrom: %s\nTo: %s\nOrder Type: Move\n%s going to execute the order from %s.", getSrc().getName(), getReceiverEntity().getName(), getSrc().getName(), getReceiverEntity().getName());
        }else if (order == MessageType.FOLLOW_ORDER){
            return String.format("Receive The Order Message:\nFrom: %s\nTo: %s\nOrder Type: Follow\n%s going to execute the order from %s.", getSrc().getName(), getReceiverEntity().getName(), getSrc().getName(), getReceiverEntity().getName());
        }else return null;
    }

    @Override
    public String getMsgDetail() {
        return receiveDetail();
    }
}
