package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

public class Message {

    private VCSApp app;
    private Entity src, receiver;
    private int counter = 0;

    public enum MessageType{
        ATTACK_ORDER,
        MOVE_ORDER,
        FOLLOW_ORDER,
        ENTITY_INFO
    }

    public MessageType type;

    public Message(MessageType type, VCSApp app, Entity src, Entity receiver){
        this.type = type;
        this.app = app;
        this.src = src;
        this.receiver = receiver;
    }
    public VCSApp getApp(){
        return app;
    }

    public Entity getSrc(){
        return src;
    }

    public Entity getReceiverEntity(){
        return receiver;
    }

    public int getCounter(){
        return counter;
    }

    public void setCounter(int c){
        counter = c;
    }

}
