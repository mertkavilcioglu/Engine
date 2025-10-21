package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

public class AttackMsg extends Message {

    private Entity attackTarget;
    //private String attackCode = "J12.1";

    public AttackMsg(VCSApp app, Entity src, Entity receiver, Entity attackTarget) {
        super(MessageType.ATTACK_ORDER, app, src, receiver, (String.format("%s: %s",src, "J12.1")));
        this.attackTarget = attackTarget;

    }

    public Entity getTarget(){
        return attackTarget;
    }

    @Override
    public String getMsgDetail() {
        return String.format("Attack Message:\nFrom: %s\nTo: %s\nTarget: %s", getSrc().getName(), getReceiverEntity().getName(), attackTarget.getName());
    }
}
