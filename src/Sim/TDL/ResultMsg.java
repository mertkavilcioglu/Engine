package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Sim.Orders.Order;
import UI.UIColorManager;

import java.awt.*;

public class ResultMsg extends Message{
    private boolean isDone;
    Order.OrderType orderType;

    public ResultMsg(VCSApp app, Entity src, Entity receiver, boolean isDone, Order.OrderType orderType) {
        super(MessageType.ORDER_RESULT, app, src.getId(), receiver.getId(), src.getId(), "J13.0");
        this.isDone = isDone;
        this.orderType = orderType;
    }

    public String resultDetail(){
        if (isDone){
            return String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Type: %s\nOrder Status: Done", getSrcID(), getTargetID(), orderType.getName());
        } else return String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Type: %s\nOrder Status: Not Done", getSrcID(), getTargetID(), orderType.getName());
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

    @Override
    public Message copy() {
        return null;
    }
}
