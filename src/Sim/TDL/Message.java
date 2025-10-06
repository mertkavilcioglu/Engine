package Sim.TDL;

public class Message {

    public enum MessageType{
        ATTACK_ORDER,
        MOVE_ORDER,
        FOLLOW_ORDER,
        ENTITY_INFO
    }

    public MessageType type;

    public Message(MessageType type){
        this.type = type;
    }
}
