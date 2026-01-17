package Sim.TDL;

import App.VCSApp;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class Message {

    private static int counter = 0;
    private final int msgID;
    private VCSApp app;
    private String srcID, targetID; // yakıştı mı?!
    private List<String> targetIDList;
    private final String msg;
    private ArrayList<String> messages;
    private String msg1;
    private String msg2;

    public enum MessageType{
        ATTACK_ORDER(MessageClass.J12),
        MOVE_ORDER(MessageClass.J12),
        FOLLOW_ORDER(MessageClass.J12),
        ENTITY_INFO(MessageClass.J2),
        RECEIVE_INFO(MessageClass.J0),
        ORDER_RESULT(MessageClass.J13),
        SURVEILLANCE_MSG(MessageClass.J3),
        MISSION_START(MessageClass.J13),
        KNOWN_INFO(MessageClass.J2),
        RELAY(MessageClass.J0),
        MISSION_ABORT(MessageClass.J12);

        private MessageClass msgClass;

        MessageType(MessageClass msgClass){
            this.msgClass = msgClass;
        }
        public MessageClass getMessageClass(){
            return msgClass;
        }
    }

    public enum MessageClass{
        ALL("ALL"),
        J0("J0"), // Relay, ACK,
        J2("J2"), // PPLI
        J3("J3"), // Surveillance & Track
        J12("J12"), // Attack, Follow, Move, Abort
        J13("J13"), // Mission Start, Result
        ;
        private String messageClass;

        MessageClass(String messageClass){
            this.messageClass = messageClass;
        }
        public String getMessageClass(){
            return messageClass;
        }

    }



    public MessageType type;

    public Message(MessageType type, VCSApp app, String srcID, String targetID, String msg1, String msg2){
        this.type = type;
        this.app = app;
        this.srcID = srcID;
        this.targetID = targetID;
        this.msg1 = msg1;
        this.msg2 = msg2;
        msgID = counter++;

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        this.msg = String.format("[%s] %s:",formattedDate, msg1);

        messages = new ArrayList<>();
        messages.add(msg);
        messages.add(msg2);



        //app.logPanel.addMsgToLog(this); TODO
    }

    protected Message(Message other){
        this.type = other.type;
        this.app = other.getApp();
        this.srcID = other.getSrcID();
        this.targetID = other.getTargetID();
        this.msg1 = other.msg1;
        this.msg2 = other.msg2;
        msgID = counter++;

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        this.msg = String.format("[%s] %s:",formattedDate, msg1);

        messages = new ArrayList<>();
        messages.add(msg);
        messages.add(msg2);
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

    public String getMsg(){
        return msg;
    }

    public ArrayList<String> getMsg2() {
        return messages;
    }

    public abstract String getMsgDetail();
    public abstract Color getColor();

    public void setTargetID(String tId){
        this.targetID = tId;
    }

    public abstract Message copy();

    public int getMsgID(){
        return msgID;
    }
}
