package Sim.TDL;

import App.VCSApp;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class Message {

    private VCSApp app;
    private String srcID, targetID;
    private List<String> targetIDList;
    private int counter = 0;
    private final String msg;
    private ArrayList<String> messages;
    private final boolean isFirstInfo = true;

    public enum MessageType{
        ATTACK_ORDER,
        MOVE_ORDER,
        FOLLOW_ORDER,
        ENTITY_INFO,
        RECEIVE_INFO,
        ORDER_RESULT,
        SURVEILLANCE_MSG,
        MISSION_START,
        KNOWN_INFO
    }

    public MessageType type;

    public Message(MessageType type, VCSApp app, String srcID, String targetID, String msg1, String msg2){
        this.type = type;
        this.app = app;
        this.srcID = srcID;
        this.targetID = targetID;

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        this.msg = String.format("[%s] %s:",formattedDate, msg1);

        messages = new ArrayList<>();
        messages.add(msg);
        messages.add(msg2);



        //app.logPanel.addMsgToLog(this); TODO
    }
    public Message(MessageType type, VCSApp app, String  srcID, List<String> receivers, String msg1, String msg2){
        this.type = type;
        this.app = app;
        this.srcID = srcID;
        this.targetIDList = receivers;

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        this.msg = String.format("[%s] %s:",formattedDate, msg1);

        messages = new ArrayList<>();
        messages.add(msg);
        messages.add(msg2);

//        if (src.getInfoMsgSendEntities().isEmpty()){
//            isFirstInfo = true;
//            infoToLog(isFirstInfo);
//        }

    }
    public VCSApp getApp(){
        return app;
    }

    public String getSrcID(){
        return srcID;
    }

    public String getTargetID(){
        return targetID;
    }

    public List<String> getTargetIDList(){
        return targetIDList;
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

    public ArrayList<String> getMsg2() {
        return messages;
    }

//    public void infoToLog(boolean isFirstInfo){
//        if (isFirstInfo){

// TODO:           app.logPanel.addMsgToLog(this);

//            for (Entity e : receiverList){
//                if (!src.getInfoMsgSendEntities().contains(e)) src.setInfoMsgSendEntities(e);
//            }
//        }
//        isFirstInfo = false;
//    }

    public abstract String getMsgDetail();
    public abstract Color getColor();

    public void setTargetID(String tId){
        targetID = tId;
    }
}
