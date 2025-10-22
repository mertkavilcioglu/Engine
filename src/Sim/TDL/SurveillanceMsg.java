package Sim.TDL;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

public class SurveillanceMsg extends Message{
    private Entity src;
    private String targetID;
    private String seenID;
    private String seenName;
    private Entity.Side seenSide;
    private Vec2int seenPos;
    private Vec2int seenSpeed;
    private Entity.Type seenType;

    public SurveillanceMsg(VCSApp app, Entity src, String targetID, Entity seenEntity) {
        super(MessageType.SURVEILLANCE_MSG, app, src.getId(), targetID, (src.getName() + (": ")));
        this.seenID = seenEntity.getId();
        this.seenName = seenEntity.getName();
        this.seenSide = seenEntity.getSide();
        this.seenPos = seenEntity.getPos();
        this.seenSpeed = seenEntity.getSpeed();
        this.seenType = seenEntity.getType();
        this.src = src;
        this.targetID = targetID;
    }

    @Override
    public String getMsgDetail() {
        return String.format(
                "Surveillance Message:\n" +
                        "From: %s\n" +
                        "To: %s\n" +
                        "Seen Entity: %s\n" +
                        "Type: %s\n" +
                        "Position: %s\n" +
                        "Speed: %s\n",
                src.getId(), targetID, seenID, seenType.getName(),
                seenPos.toString(), seenSpeed.toString()
        );
    }
}
