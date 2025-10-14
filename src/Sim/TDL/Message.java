package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

import java.util.List;

public abstract class Message {

    private VCSApp app;
    private Entity src, receiver;
    private List<Entity> receiverList;
    private int counter = 0;
    private String msg;

    public enum MessageType{
        ATTACK_ORDER,
        MOVE_ORDER,
        FOLLOW_ORDER,
        ENTITY_INFO
    }

    public MessageType type;

    public Message(MessageType type, VCSApp app, Entity src, Entity receiver, String msg){
        this.type = type;
        this.app = app;
        this.src = src;
        this.receiver = receiver;
        this.msg = msg;
        app.logPanel.addMsgToLog(this);
    }
    public Message(MessageType type, VCSApp app, Entity src, List<Entity> receivers, String msg){
        this.type = type;
        this.app = app;
        this.src = src;
        this.receiverList = receivers;
        this.msg = msg;
        app.logPanel.addMsgToLog(this);
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

    public List<Entity> getReceiverList(){
        return receiverList;
    }

    public int getCounter(){
        return counter;
    }

    public void setCounter(int c){
        counter = c;
    }

    public String getMsg(){
        return msg;
    }

    public abstract String getMsgDetail();

}
