package Sim.TDL;

import Sim.Entity;

public class TDLReceiver {

    private Entity source;
    public TDLReceiver(Entity source){
        this.source = source;
    }

    public void receiveMessage(Message msg){
        if(msg.type == Message.MessageType.ATTACK_ORDER){
            //entity.add new attack order
        }
    }
}
