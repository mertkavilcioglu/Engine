package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

public class AttackMsg extends Message {

    private Entity attackTarget;
    //private String attackCode = "J12.1";

    public AttackMsg(VCSApp app, String srcID, String receiverID, Entity attackTarget) {
        super(MessageType.ATTACK_ORDER, app, srcID, receiverID, (String.format("%s: %s",srcID, "J12.1")));
        this.attackTarget = attackTarget;

    }

    public Entity getTarget(){
        return attackTarget;
    }

    @Override
    public String getMsgDetail() {
        return String.format("Attack Mission Assignment Message:\nFrom: %s\nTo: %s\nTarget: %s", getSrcID(), getTargetID(), attackTarget.getName());
    }
}
