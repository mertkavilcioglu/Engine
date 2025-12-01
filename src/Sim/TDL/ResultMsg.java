package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Sim.Orders.Order;
import UI.UIColorManager;

import java.awt.*;

public class ResultMsg extends Message{
    private int finishStat; // 0 for success, 404 for attack target not found, 408 for follow time out
    Order.OrderType orderType;

    public ResultMsg(VCSApp app, Entity src, Entity receiver, int finishStat, Order.OrderType orderType) {
        super(MessageType.ORDER_RESULT, app, src.getId(), receiver.getId(), src.getId(), "J13.0");
        this.finishStat = finishStat;
        this.orderType = orderType;
    }

    public String resultDetail(){
        String detailMsg = null;
        switch (orderType){
            case ATTACK:
                if (finishStat == 0){
                    detailMsg = String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Type: %s\nFinish Report: Target Destroy Successfully.", getSrcID(), getTargetID(), orderType.getName());
                } else if (finishStat == 404){
                    detailMsg = String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Type: %s\nFinish Report: Target Could Not Find at the Known Spot.", getSrcID(), getTargetID(), orderType.getName());
                } else detailMsg = String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Type: %s\nFinsh Report: Invalid.", getSrcID(), getTargetID(), orderType.getName());
                break;
            case FOLLOW:
                if (finishStat == 0){
                    detailMsg = String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Type: %s\nFinsh Report: Target Followed For the Time Limit.", getSrcID(), getTargetID(), orderType.getName());
                } else if (finishStat == 408){
                    detailMsg = String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Type: %s\nFinsh Report: Following Terminated Because Time Limit Reached.", getSrcID(), getTargetID(), orderType.getName());
                } else return String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Type: %s\nFinsh Report: Invalid.", getSrcID(), getTargetID(), orderType.getName());
                break;
            case MOVE:
                if (finishStat == 0){
                    detailMsg = String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Type: %s\nFinsh Report: Reached the Target Coordinate.", getSrcID(), getTargetID(), orderType.getName());
                }else detailMsg = String.format("Order Result Report Message:\nFrom: %s\nTo: %s\nOrder Type: %s\nFinsh Report: Invalid.", getSrcID(), getTargetID(), orderType.getName());
                break;
        }
        return detailMsg;
    }

    public int getOrderResult(){
        return finishStat;
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
