package Sim.TDL;

import App.VCSApp;

public class MissionStartMsg extends Message{
    private String missionType;

    public MissionStartMsg(VCSApp app, String srcID, String targetID, String msg) {
        super(MessageType.MISSION_START, app, srcID, targetID, String.format("%s: %s", srcID, msg));
        missionType = msg;
    }

    @Override
    public String getMsgDetail() {
        String missionName;
        if (missionType.equals("J13.1")){
            missionName = "Offensive";
        } else if (missionType.equals("J13.2")) {
            missionName = "Escort";
        }else missionName = "Move";
        return String.format("%s Mission Start Message:\n" +
                "Mission Perform By: %s\n" +
                "Mission Given By: %s\n" +
                        "Unit going to execute the order.",
                missionName, getSrcID(), getTargetID());
    }
}
