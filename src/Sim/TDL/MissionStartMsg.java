package Sim.TDL;

import App.VCSApp;
import UI.UIColorManager;

import java.awt.*;

public class MissionStartMsg extends Message{
    private final String missionType;

    public MissionStartMsg(VCSApp app, String srcID, String targetID, String msg) {
        super(MessageType.MISSION_START, app, srcID, targetID, srcID, msg);
        missionType = msg;
    }

    @Override
    public String getMsgDetail() {
        String missionName;
        if (missionType.equals("J13.1")){
            missionName = "Attack";
        } else if (missionType.equals("J13.2")) {
            missionName = "Escort";
        }else missionName = "Move";
        return String.format("""
                        %s Mission Start Message:
                        Mission Perform By: %s
                        Mission Given By: %s
                        Unit going to execute the order.""",
                missionName, getSrcID(), getTargetID());
    }

    @Override
    public Color getColor() {
        return UIColorManager.J13_COLOR;
    }

    @Override
    public Message copy() {
        return null;
    }
}
