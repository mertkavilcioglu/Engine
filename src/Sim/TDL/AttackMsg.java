package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

import java.awt.*;

public class AttackMsg extends Message {

    private String attackTargetID;
    //private String attackCode = "J12.1";

    public AttackMsg(VCSApp app, String srcID, String receiverID, String attackTargetID) {
        super(MessageType.ATTACK_ORDER, app, srcID, receiverID, srcID, "J12.1");
        this.attackTargetID = attackTargetID;
        //TODO: HEDEFE HQ SALDIRIYOR ???????
    }

    public String  getAttackTargetID(){
        return attackTargetID;
    }

    @Override
    public String getMsgDetail() {
        return String.format("Attack Mission Assignment Message:\nFrom: %s\nTo: %s\nTarget: %s", getSrcID(), getTargetID(), getAttackTargetID());
    }

    @Override
    public Color getColor() {
        return Color.YELLOW;
    }
}
