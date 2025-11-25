package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import UI.UIColorManager;
import Vec.Vec2int;

import java.awt.*;

public class SurveillanceMsg extends Message{
    private Entity src;
    private String targetID;
    private String hostileID;
    private String hostileName;
    private Entity.Side hostileSide;
    private Vec2int hostilePos;
    private Vec2int hostileSpeed;
    private Entity.Type hostileType;

    public SurveillanceMsg(VCSApp app, Entity src, String targetID, Entity trackEntity) {
        super(MessageType.SURVEILLANCE_MSG, app, src.getId(), targetID, src.getId(), trackEntity.getType().getTrackCode());
        this.hostileID = trackEntity.getId();
        this.hostileName = trackEntity.getName();
        this.hostileSide = trackEntity.getSide();
        this.hostilePos = trackEntity.getPos();
        this.hostileSpeed = trackEntity.getSpeed();
        this.hostileType = trackEntity.getType();
        this.src = src;
        this.targetID = targetID;
    }

    public SurveillanceMsg(SurveillanceMsg other){
        super(other);
        this.hostileID = other.getHostileID();
        this.hostileName = other.getHostileName();
        this.hostilePos = other.getHostilePos();
        this.hostileSide = other.getHostileSide();
        this.hostileSpeed = other.getHostileSpeed();
        this.hostileType = other.getHostileType();
        this.src = other.src;
        this.targetID = other.getTargetID();
    }

    private String getSrcName(){
        return src.getName();
    }

    public String getHostileID(){
        return hostileID;
    }

    public String getHostileName(){
        return hostileName;
    }

    public Entity.Side getHostileSide() {
        return hostileSide;
    }

    public Entity.Type getHostileType() {
        return hostileType;
    }

    public Vec2int getHostilePos() {
        return hostilePos;
    }

    public Vec2int getHostileSpeed() {
        return hostileSpeed;
    }

    @Override
    public String getMsgDetail() {
        return String.format(
                "Surveillance Message:\n" +
                        "From: %s\n" +
                        "Hostile's Informations:\n" +
                        "Entity: %s\n" +
                        "Type: %s\n" +
                        "Position: %s\n" +
                        "Speed: %s\n",
                src.getId(), hostileID, hostileType.getName(),
                hostilePos.toString(), hostileSpeed.toString()
        );
    }

    @Override
    public Color getColor() {
        return UIColorManager.J3_COLOR;
    }

    @Override
    public Message copy() {
        return new SurveillanceMsg(this);
    }
}
