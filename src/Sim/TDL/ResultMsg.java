package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

public class ResultMsg extends Message{
//TODO içini doldur
    private boolean isDone;

    public ResultMsg(VCSApp app, Entity src, Entity receiver, boolean isDone) {
        super(MessageType.ORDER_RESULT, app, src, receiver, (src + ": Result Msg"));
        this.isDone = isDone;
    }

    public String resultDetail(){
        if (isDone){
            return String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Status: Done", getSrc().getName(), getReceiverEntity().getName());
        } else return String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Status: Not Done", getSrc().getName(), getReceiverEntity().getName());
    }

    public boolean getOrderResult(){
        return isDone;
    }

    @Override
    public String getMsgDetail() {
        return resultDetail();
    }
}
