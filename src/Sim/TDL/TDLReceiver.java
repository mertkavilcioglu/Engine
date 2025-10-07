package Sim.TDL;

import Sim.Entity;
import Sim.Orders.Attack;
import Sim.Orders.Follow;
import Sim.Orders.Move;

import javax.sound.midi.spi.SoundbankReader;

public class TDLReceiver {

    private Entity source;
    public TDLReceiver(Entity source){
        this.source = source;
    }

    public void receiveMessage(Message msg){
        if(msg.type == Message.MessageType.ATTACK_ORDER){
            source.addOrder(new Attack(((AttackMsg) msg).getApp(), ((AttackMsg) msg).getSrc(),
                    ((AttackMsg) msg).getTarget()));
        }
        else if(msg.type == Message.MessageType.FOLLOW_ORDER){
            source.addOrder(new Follow(((FollowMsg) msg).getApp(), ((FollowMsg) msg).getSrc(),
                    ((FollowMsg) msg).getTarget(), ((FollowMsg) msg).getTime()));
        }
        else if(msg.type == Message.MessageType.MOVE_ORDER){
            source.addOrder(new Move(((MoveMsg) msg).getApp(), ((MoveMsg) msg).getSrc(),
                    ((MoveMsg) msg).getPos()));
        }
        else if(msg.type == Message.MessageType.ENTITY_INFO){
            //TODO: ENTITY'YI KENDI LOCAL WORLDUNE EKLE,
            // DIREK GERCEK ENTITYYI REFERANS OLARAK EKLEYEBILIRSIN DAHA KOLAY OLUR
            // VEYA LOCALCREATE FONKSIYONU YAZIP ELDEKI INFO ILE YENI BIR ENTITY OLUSTURABILIRSIN
            // AMA BU SEFER DE DELETE ICIN FALAN KOPYA ENTITY'DE SOURCE ENTITY REF TUTMAK GEREKEBILIR 
        }
    }
}
