package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import UI.UIColorManager;

import java.awt.*;

public class ResultMsg extends Message{
//TODO içini doldur
    private boolean isDone;

    public ResultMsg(VCSApp app, Entity src, Entity receiver, boolean isDone) {
        super(MessageType.ORDER_RESULT, app, src.getId(), receiver.getId(), src.getId(), "J13.0");
        this.isDone = isDone;
    }

    public String resultDetail(){
        if (isDone){
            return String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Status: Done", getSrcID(), getTargetID());
        } else return String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Status: Not Done", getSrcID(), getTargetID());
    }

    public boolean getOrderResult(){
        return isDone;
    }

    @Override
    public String getMsgDetail() {
        return resultDetail();
    }

    @Override
    public Color getColor() {
        return UIColorManager.J13_COLOR;
    }
}
