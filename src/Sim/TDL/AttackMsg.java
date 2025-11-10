package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

public class AttackMsg extends Message {

    private String attackTargetID;
    //private String attackCode = "J12.1";

    public AttackMsg(VCSApp app, String srcID, String receiverID, String attackTargetID) {
        super(MessageType.ATTACK_ORDER, app, srcID, receiverID, (String.format("%s: %s",srcID, "J12.1")));
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
}
