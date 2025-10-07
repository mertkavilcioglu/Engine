package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

public class AttackMsg extends Message {

    private Entity attackTarget;

    public AttackMsg(VCSApp app, Entity src, Entity receiver, Entity attackTarget) {
        super(MessageType.ATTACK_ORDER, app, src, receiver);
        this.attackTarget = attackTarget;

    }

    public Entity getTarget(){
        return attackTarget;
    }
}
