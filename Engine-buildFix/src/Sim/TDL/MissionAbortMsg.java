package Sim.TDL;

import App.VCSApp;
import Sim.Orders.Order;

import java.awt.*;

public class MissionAbortMsg extends Message{
    private final Order.OrderType orderType;

    public MissionAbortMsg(VCSApp app, String srcID, String targetID, Order.OrderType orderType) {
        super(MessageType.MISSION_ABORT, app, srcID, targetID, srcID, "J12.0");
        this.orderType = orderType;
    }

    @Override
    public String getMsgDetail() {
        return String.format("""
                Mission Abort Message:
                From: %s
                To: %s
                Mission Type: %s
                """, getSrcID(), getTargetID(), orderType.getName());
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }

    @Override
    public Message copy() {
        return null;
    }
}
