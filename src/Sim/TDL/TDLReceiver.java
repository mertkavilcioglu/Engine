package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Sim.Orders.Attack;
import Sim.Orders.Follow;
import Sim.Orders.Move;

public class TDLReceiver {

    private Entity source;

    public TDLReceiver(Entity source){
        this.source = source;
    }

    public void receiveMessage(Message msg){
        if(msg.type == Message.MessageType.ATTACK_ORDER){
            source.addOrder(new Attack(msg.getApp(), msg.getReceiverEntity(),
                    ((AttackMsg) msg).getTarget()));
        }
        else if(msg.type == Message.MessageType.FOLLOW_ORDER){
            source.addOrder(new Follow(msg.getApp(), msg.getReceiverEntity(),
                    ((FollowMsg) msg).getFollowTarget(), ((FollowMsg) msg).getTime()));

        }
        else if(msg.type == Message.MessageType.MOVE_ORDER){
            source.addOrder(new Move(msg.getApp(), msg.getReceiverEntity(),
                    ((MoveMsg) msg).getPos()));
        }
        else if(msg.type == Message.MessageType.ENTITY_INFO){
            source.addKnownEntity(msg.getSrc());
            //TODO: ENTITY'YI KENDI LOCAL WORLDUNE EKLE,
            // DIREK GERCEK ENTITYYI REFERANS OLARAK EKLEYEBILIRSIN DAHA KOLAY OLUR
            // VEYA LOCALCREATE FONKSIYONU YAZIP ELDEKI INFO ILE YENI BIR ENTITY OLUSTURABILIRSIN
            // AMA BU SEFER DE DELETE ICIN FALAN KOPYA ENTITY'DE SOURCE ENTITY REF TUTMAK GEREKEBILIR
        }
        else if (msg.type == Message.MessageType.RECEIVE_INFO) {
            msg.getApp().debugLog("Message arrived successfully.");
            //TODO orderının receivelendiğini öğreninice nolcak bilmiyorum
        }
        else if (msg.type == Message.MessageType.ORDER_RESULT) {
            ResultMsg rm = (ResultMsg) msg;
            if (rm.getOrderResult()){
                msg.getApp().debugLog("Order done!");
                //order tamamlanmış okey
            } else {
                msg.getApp().debugLog("Order not done!");
                //TODO order tamalanamamış, nedeni ve ne orderı olduğuna bakılıp tekrar emir verilinebilir
            }
        }
    }


}
