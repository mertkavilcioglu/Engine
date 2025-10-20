package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class Message {

    private VCSApp app;
    private Entity src, receiver;
    private List<Entity> receiverList;
    private int counter = 0;
    private String msg;
    private boolean isFirstInfo = true;

    public enum MessageType{
        ATTACK_ORDER,
        MOVE_ORDER,
        FOLLOW_ORDER,
        ENTITY_INFO,
        RECEIVE_INFO,
        ORDER_RESULT
    }

    public MessageType type;

    public Message(MessageType type, VCSApp app, Entity src, Entity receiver, String msg){
        this.type = type;
        this.app = app;
        this.src = src;
        this.receiver = receiver;

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        this.msg = String.format("[%s] %s",formattedDate, msg);

        app.logPanel.addMsgToLog(this);
    }
    public Message(MessageType type, VCSApp app, Entity src, List<Entity> receivers, String msg){
        this.type = type;
        this.app = app;
        this.src = src;
        this.receiverList = receivers;

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);

        this.msg = String.format("[%s] %s",formattedDate, msg);

        if (src.getInfoMsgSendEntities().isEmpty()){
            isFirstInfo = true;
            infoToLog(isFirstInfo);
        } else {
            for (Entity entity : receiverList){
                if (!src.getInfoMsgSendEntities().contains(entity)) {
                    isFirstInfo = true;
                    infoToLog(isFirstInfo);
                    return;
                }
                else isFirstInfo = false;
            }
        }

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

    public void infoToLog(boolean isFirstInfo){
        if (isFirstInfo){
            app.logPanel.addMsgToLog(this);
            for (Entity e : receiverList){
                if (!src.getInfoMsgSendEntities().contains(e)) src.setInfoMsgSendEntities(e);
            }
        }
        isFirstInfo = false;
    }

    public abstract String getMsgDetail();

}
